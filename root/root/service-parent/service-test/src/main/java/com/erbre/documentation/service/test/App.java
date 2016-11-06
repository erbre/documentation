package com.erbre.documentation.service.test;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {

        ChronoLoggerTemplate chrono = new ChronoLoggerTemplate() {

            @Override
            protected void run() throws Exception {
                MultiThreadTestManager.start(new DocumentationTestAll(), 500);
            }
        };
        chrono.execute("App");
    }

}
