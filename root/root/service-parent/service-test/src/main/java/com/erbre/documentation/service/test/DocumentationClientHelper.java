package com.erbre.documentation.service.test;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBElement;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.erbre.documentation.model.PersonListType;
import com.erbre.documentation.model.PersonType;
import com.erbre.documentation.person_service.v1.PersonServicePortType;
import com.erbre.documentation.person_service_message.v1.FindAllRequestType;
import com.erbre.documentation.person_service_message.v1.FindAllResponseType;
import com.erbre.documentation.person_service_message.v1.ObjectFactory;
import com.erbre.documentation.service.contrat.PersonServiceRest;

public final class DocumentationClientHelper {

    private DocumentationClientHelper() {
    }

    private static Logger LOGGER = LoggerFactory.getLogger(DocumentationClientHelper.class.getName());

    public static void testRest(ClassPathXmlApplicationContext context) {
        LOGGER.info("******** START REST TEST ********");
        try {
            PersonServiceRest service = JAXRSClientFactory
                    .create("http://localhost:8081/service-war/rest/person-resource/", PersonServiceRest.class);
            WebClient.client(service).accept(MediaType.APPLICATION_XML_TYPE);
            LOGGER.info(String.format("Sending Service Request [%s]", service));
            JAXBElement<PersonListType> persons = service.findAll();
            LOGGER.info(String.format("Received Service Response [%s]", persons));
            persons.getValue().getItems().forEach(p -> LOGGER.info(String.format("Person Found [%s]", p.toString())));

            LOGGER.info(String.format("Sending Service Request [%s]", service));
            JAXBElement<PersonType> person = service.find(1l);
            LOGGER.info(String.format("Received Service Response [%s]", person));
            LOGGER.info(String.format("Person Found [%s]", person.getValue().toString()));

            LOGGER.info("REST TEST OK");
        } catch (Exception ex) {
            LOGGER.error("REST TEST KO", ex);
        }
        LOGGER.info("******** END REST TEST ********");

    }

    public static void testRestSpring(ClassPathXmlApplicationContext context) {
        LOGGER.info("******** START REST Spring TEST ********");
        try {
            Object bean = context.getBean(PersonServiceRest.class);
            PersonServiceRest service = (PersonServiceRest) bean;
            WebClient.client(service).accept(MediaType.APPLICATION_XML_TYPE);
            LOGGER.info(String.format("Sending Service Request [%s]", service));
            JAXBElement<PersonListType> persons = service.findAll();
            LOGGER.info(String.format("Received Service Response [%s]", persons));
            persons.getValue().getItems().forEach(p -> LOGGER.info(String.format("Person Found [%s]", p.toString())));

            LOGGER.info(String.format("Sending Service Request [%s]", service));
            JAXBElement<PersonType> person = service.find(1l);
            LOGGER.info(String.format("Received Service Response [%s]", person));
            LOGGER.info(String.format("Person Found [%s]", person.getValue().toString()));

            LOGGER.info("REST TEST Spring OK");
        } catch (Exception ex) {
            LOGGER.error("REST TEST Spring KO", ex);
        }
        LOGGER.info("******** END REST TEST ********");

    }

    public static void testSoap(ClassPathXmlApplicationContext context) {
        LOGGER.info("******** START SOAP TEST ********");
        try {
            PersonServicePortType service = (PersonServicePortType) context.getBean("personServiceClientSoap");
            ObjectFactory factory = new ObjectFactory();
            FindAllRequestType request = factory.createFindAllRequestType();
            LOGGER.info(String.format("Sending Service Request [%s]", request.toString()));
            FindAllResponseType response = service.findAllOperation(request);
            LOGGER.info(String.format("Received Service Response [%s]", response.toString()));
            response.getResult().getItems().forEach(p -> LOGGER.info(String.format("Person Found [%s]", p.toString())));
            LOGGER.info("SOAP TEST OK");
        } catch (Exception ex) {
            LOGGER.error("SOAP TEST KO", ex);
        }
        LOGGER.info("******** END SOAP TEST ********");

    }
}
