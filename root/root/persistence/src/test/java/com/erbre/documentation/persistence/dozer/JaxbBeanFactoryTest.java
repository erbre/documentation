package com.erbre.documentation.persistence.dozer;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.erbre.documentation.model.AddressType;
import com.erbre.documentation.model.CountryType;
import com.erbre.documentation.model.ObjectFactory;
import com.erbre.documentation.model.PersonType;
import com.erbre.documentation.utils.dozer.JaxbBeanFactory;

/**
 * Unit test for simple App.
 */
public class JaxbBeanFactoryTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(JaxbBeanFactoryTest.class);

    @Test
    public void test() {
        try {
            List<String> factories = new ArrayList<String>();
            factories.add(ObjectFactory.class.getName());
            JaxbBeanFactory factory = new JaxbBeanFactory();
            factory.setFactories(factories);

            testCreation(factory, AddressType.class);
            testCreation(factory, CountryType.class);
            testCreation(factory, PersonType.class);
            testCreation(factory, String.class);

        } catch (Exception ex) {
            LOGGER.error("Error in testing", ex);
            Assert.assertNull(ex);
        }
    }

    @Test
    public void testEchecFindClass() {
        try {
            List<String> factories = new ArrayList<String>();
            String test = "toto";
            factories.add(test);
            JaxbBeanFactory factory = new JaxbBeanFactory();
            IllegalArgumentException ex = null;
            try {
                factory.setFactories(factories);
            } catch (IllegalArgumentException e) {
                ex = e;
            }
            Assert.assertNotNull(ex);
            Assert.assertEquals(
                    String.format(JaxbBeanFactory.FAILED_TO_FIND_CLASS_FOR_JAXB_FACTORY_S_CHECK_CLASSPATH, test),
                    ex.getMessage());

        } catch (Exception ex) {
            LOGGER.error("Error in testing", ex);
            Assert.assertNull(ex);
        }
    }

    class TestClass {
        private TestClass() {

        }
    }

    @Test
    public void testEchecInstanciateClass() {
        try {
            List<String> factories = new ArrayList<String>();
            String test = TestClass.class.getName();
            factories.add(test);
            JaxbBeanFactory factory = new JaxbBeanFactory();
            IllegalArgumentException ex = null;
            try {
                factory.setFactories(factories);
            } catch (IllegalArgumentException e) {
                ex = e;
            }
            Assert.assertNotNull(ex);
            Assert.assertEquals(
                    String.format(JaxbBeanFactory.FAILED_TO_INSTANCIATE_OBJECT_FOR_CLASS_FOR_JAXB_FACTORY_S, test),
                    ex.getMessage());

        } catch (Exception ex) {
            LOGGER.error("Error in testing", ex);
            Assert.assertNull(ex);
        }
    }

    @Test
    public void testEchecInstanciateBean() {
        try {
            JaxbBeanFactory factory = new JaxbBeanFactory();
            Object bean = factory.createBean(null, null, TestClass.class.getName());

            Assert.assertNull(bean);

        } catch (Exception ex) {
            LOGGER.error("Error in testing", ex);
            Assert.assertNull(ex);
        }
    }

    private void testCreation(JaxbBeanFactory factory, Class<?> class1) {
        Object bean = factory.createBean(null, null, class1.getName());
        Assert.assertNotNull(bean);
        Assert.assertEquals(bean.getClass(), class1);

    }

}
