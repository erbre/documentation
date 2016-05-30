package com.erbre.documentation.service.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.erbre.documentation.person_service.v1.PersonServicePortType;
import com.erbre.documentation.person_service_message.v1.FindAllRequestType;
import com.erbre.documentation.person_service_message.v1.FindAllResponseType;
import com.erbre.documentation.person_service_message.v1.ObjectFactory;

/**
 * Hello world!
 *
 */
public class App {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class.getName());

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = null;
        try {
            context = new ClassPathXmlApplicationContext("spring/application-context.xml");
            PersonServicePortType service = (PersonServicePortType) context.getBean("personServiceClient");
            ObjectFactory factory = new ObjectFactory();
            FindAllRequestType request = factory.createFindAllRequestType();
            LOGGER.info(String.format("Sending Service Request [%s]", request.toString()));
            FindAllResponseType response = service.findAllOperation(request);
            LOGGER.info(String.format("Received Service Response [%s]", response.toString()));

            response.getResult().getItems().forEach(p -> LOGGER.info(String.format("Person Found [%s]", p.toString())));
        } finally {
            if (context != null) {
                context.close();
            }
        }
    }
}
