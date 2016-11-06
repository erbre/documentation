package com.erbre.documentation.persistence;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.erbre.documentation.model.AddressType;
import com.erbre.documentation.model.CountryType;
import com.erbre.documentation.model.ObjectFactory;
import com.erbre.documentation.model.PersonType;
import com.erbre.documentation.persistence.service.PersonObjectManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/test-application-context.xml")
@Rollback
public class PersonServiceTest extends AbstractTransactionalJUnit4SpringContextTests {
    @Inject
    private PersonObjectManager service;

    @Before
    public void setUp() {

    }

    @Test
    public void testRead() {

        Iterable<PersonType> list = service.findAll();
        if (list != null) {
            for (PersonType p : list) {
                PersonType p2 = service.read(p);
                assertEquals(p, p2);

            }
        }else {
            Assert.fail();
        }

        PersonType person = new ObjectFactory().createPersonType();
        person.setId(1l);
        PersonType read = service.read(person);
        Assert.assertEquals("id", read.getId(), 1);

    }

    private void assertEquals(PersonType p, PersonType p2) {
        if (p == null) {
            Assert.assertNull(p2);
        } else {
            Assert.assertNotNull(p);
            Assert.assertEquals(p.getId(), p2.getId());
            Assert.assertEquals(p.getFirstName(), p2.getFirstName());
            Assert.assertEquals(p.getLastName(), p2.getLastName());
            assertEquals(p.getAddress(), p2.getAddress());
        }

    }

    private void assertEquals(AddressType a, AddressType a2) {
        if (a == null) {
            Assert.assertNull(a2);
        } else {
            Assert.assertNotNull(a);
            Assert.assertEquals(a.getId(), a2.getId());
            Assert.assertEquals(a.getCity(), a2.getCity());
            Assert.assertEquals(a.getStreet(), a2.getStreet());
            Assert.assertEquals(a.getZipCode(), a2.getZipCode());
            assertEquals(a.getCountry(), a2.getCountry());
        }
    }

    private void assertEquals(CountryType country, CountryType country2) {
        if (country == null) {
            Assert.assertNull(country2);
        } else {
            Assert.assertNotNull(country);
            Assert.assertEquals(country.getId(), country2.getId());
            Assert.assertEquals(country.getName(), country2.getName());
            Assert.assertEquals(country.getCode(), country2.getCode());
        }
    }

}
