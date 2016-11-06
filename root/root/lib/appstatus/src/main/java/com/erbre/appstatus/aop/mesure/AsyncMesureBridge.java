package com.erbre.appstatus.aop.mesure;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AsyncMesureBridge<T> implements MesureListener<T> {

    private Logger LOGGER = LoggerFactory.getLogger(AsyncMesureBridge.class.getName());

    private MesureListener<T> listener;

    public void setListener(MesureListener<T> listener) {
        this.listener = listener;
    }

    private final BlockingQueue<T> events;

    private ExecutorService service;

    private int maxError = -1;

    public void setMaxError(int maxError) {
        this.maxError = maxError;
    }

    @Override
    public void receive(T event) {
        // LOGGER.info("AsyncMesureBridge ask to send new Event");
        boolean success = events.offer(event);
        if (!success) {
            // LOGGER.error("No more space on AsyncMesureBridge: one event is
            // lost!!");
        }
    }

    public AsyncMesureBridge() {
        events = new LinkedBlockingQueue<>();
    }

    public void start() {
        if (service == null) {
            LOGGER.info("Starting AsyncMesureBridge executors");
            service = Executors.newSingleThreadExecutor();
            service.submit(new MyRunnable());
            LOGGER.info("AsyncMesureBridge is successfully started");
        } else {
            LOGGER.warn("AsyncMesureBridge is already started");
        }
    }

    public void stop() {
        if (service != null) {
            LOGGER.info("Stopping AsyncMesureBridge executors");
            List<Runnable> runs = service.shutdownNow();
            long timeout = 1000l;
            TimeUnit unit = TimeUnit.SECONDS;
            LOGGER.info(String.format("Waiting Termination of [%s] Thread : max waiting is [%s] [%s]",
                    runs == null ? 0 : runs.size(), timeout, unit));
            try {
                service.awaitTermination(timeout, unit);
            } catch (InterruptedException e) {
                LOGGER.warn("AsyncMesureBridge interrupted in waiting for termination");
            }
            LOGGER.info("AsyncMesureBridge is successfully stopped");
            service = null;
        } else {
            LOGGER.warn("AsyncMesureBridge is already stopped or has not been started");
        }
    }

    private class MyRunnable implements Runnable {

        @Override
        public void run() {
            LOGGER.info("AsyncMesureBridge Background Thread Start");
            boolean keepOn = true;
            int errorCount = 0;
            while (keepOn) {
                T event;
                try {
                    LOGGER.info("AsyncMesureBridge Background Thread waiting for event");
                    event = events.take();
                    listener.receive(event);
                    LOGGER.info("AsyncMesureBridge Background Thread sent event");
                } catch (InterruptedException e) {
                    LOGGER.warn("AsyncMesureBridge Background Thread is interrupted");
                    keepOn = false;
                } catch (Exception e) {
                    LOGGER.error("AsyncMesureBridge Background Thread Send Event Error", e);
                    if (maxError > 0) {
                        errorCount++;
                        LOGGER.info(String.format(
                                "AsyncMesureBridge Background Thread [%s] Error : will stop at [%s] error", errorCount,
                                maxError));
                        if (errorCount > maxError) {
                            keepOn = false;
                        }
                    }

                }
            }
            LOGGER.info("AsyncMesureBridge Background Thread End");
        }

    }

    @EventListener({ ContextRefreshedEvent.class })
    public void onContextRefreshedEvent(ContextRefreshedEvent event) {
        LOGGER.info("Spring ContextRefreshedEvent at [" + event.getTimestamp() + "]");
        if (service == null) {
            start();
        } else {
            stop();
            start();
        }

    }

    @EventListener({ ContextStartedEvent.class })
    public void onContextStartedEvent(ContextStartedEvent event) {
        LOGGER.info("Spring ContextStartedEvent at [" + event.getTimestamp() + "]");
        start();

    }

    @EventListener({ ContextClosedEvent.class })
    public void onContextClosedEvent(ContextClosedEvent event) {
        LOGGER.info("Spring ContextClosedEvent at [" + event.getTimestamp() + "]");
        stop();

    }

    @EventListener({ ContextStoppedEvent.class })
    public void onContextStoppedEvent(ContextStoppedEvent event) {
        LOGGER.info("Spring ContextStoppedEvent at [" + event.getTimestamp() + "]");
        stop();

    }

}
