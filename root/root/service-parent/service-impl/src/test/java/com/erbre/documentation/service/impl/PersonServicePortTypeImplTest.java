package com.erbre.documentation.service.impl;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.erbre.documentation.person_service_message.v1.FindAllResponseType;
import com.erbre.documentation.person_service_message.v1.ObjectFactory;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/test-application-context.xml")
@Rollback
public class PersonServicePortTypeImplTest extends AbstractTransactionalJUnit4SpringContextTests {
    @Inject
    private PersonServicePortTypeImpl service;

    @Before
    public void setUp() {

    }

    @Test
    public void testRead() {

        ObjectFactory of = new ObjectFactory();

        FindAllResponseType response = service.findAllOperation(of.createFindAllRequestType());

        response.getResult().getItems().forEach(p -> System.out.println(p));
        Assert.assertFalse("id", response.getResult().getItems().isEmpty());

    }

}
