package com.erbre.documentation.service.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestContextRunnable implements Runnable {

    private Logger LOGGER = LoggerFactory.getLogger(TestContextRunnable.class.getName());

    protected ClassPathXmlApplicationContext context;
    protected TestListener listener;
    private ContextTest test;

    public TestContextRunnable(ClassPathXmlApplicationContext context, TestListener listener, ContextTest test) {
        super();
        this.context = context;
        this.listener = listener;
        this.test = test;
    }

    @Override
    public void run() {
        String id = String.format("Thread [%s] id[%s]", Thread.currentThread().getName(),
                Thread.currentThread().getId());

        ChronoLoggerTemplate chrono = new ChronoLoggerTemplate() {

            @Override
            protected void run() throws Exception {
                test.test(context);
            }

            @Override
            protected void end() {
                listener.notifyEnd(id);
                super.end();
            }
        };
        chrono.execute(String.format("Test Thread[%s]", id));
    }
}
