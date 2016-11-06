package com.erbre.documentation.service.test;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.erbre.documentation.service.contrat.PersonServiceRest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/application-context.xml")
public class PersonServiceRestTest {

    @Inject
    private PersonServiceRest service;

    @Before
    public void setUp() {

    }

    @Test
    public void testRead() {

    }

}
