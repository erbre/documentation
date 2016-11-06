package com.erbre.documentation.service.test;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MultiThreadTestManager implements TestListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MultiThreadTestManager.class.getName());

    public static void start(ContextTest test, int threadNb) {
        ChronoLoggerTemplate template = new ChronoLoggerTemplate() {

            @Override
            protected void run() throws Exception {
                new MultiThreadTestManager().execute(test, threadNb);
            }
        };
        template.execute("MultiThreadTest");

    }

    private void execute(ContextTest test, int threadNb) {
        ClassPathXmlApplicationContext context = null;
        try {
            context = new ClassPathXmlApplicationContext("spring/application-context.xml");
            int max = threadNb;
            LOGGER.info(String.format("Test will start [%s] Threads", max));
            for (int i = 0; i < max; i++) {
                started.getAndIncrement();
                TestContextRunnable run = new TestContextRunnable(context, this, test);
                Thread thread = new Thread(run);
                thread.start();
            }
            while (started.get() > ended.get()) {
                LOGGER.info(String.format("Waiting end test : started Thread[%s] > ended Thread [%s]", started.get(),
                        ended.get()));
                try {
                    Thread.sleep(1000l);
                } catch (InterruptedException e) {
                }
            }
        } finally {
            if (context != null) {
                context.close();
            }
        }

    }

    private static final AtomicInteger started = new AtomicInteger(0);
    private static final AtomicInteger ended = new AtomicInteger(0);

    @Override
    public void notifyEnd(String id) {
        ended.getAndIncrement();

    }

}
