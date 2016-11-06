package com.erbre.documentation.service.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DocumentationTestAll implements ContextTest {

    public DocumentationTestAll() {
        super();
    }

    @Override
    public void test(ClassPathXmlApplicationContext context) {
        ChronoLoggerTemplate template = new ChronoLoggerTemplate() {
            @Override
            protected void run() throws Exception {
//                DocumentationClientHelper.testSoap(context);
                DocumentationClientHelper.testRest(context);
//                DocumentationClientHelper.testRestSpring(context);
            }
        };
        template.execute("Test All");

    }

}
