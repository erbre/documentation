package com.erbre.documentation.persistence;

import java.lang.annotation.Annotation;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Unit test for simple App.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/test-application-context.xml")
public class AppTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(AppTest.class);

	@Inject
	private ApplicationContext context;

	@Test
	public void testPersistenceSpring() {
		try {
			EntityManagerFactory factory = context.getBean(EntityManagerFactory.class);
			testPersistence(factory);
		} catch (Exception ex) {
			LOGGER.error("Error in testing", ex);
			Assert.assertNull(ex);
		}
	}

	private void testPersistence(EntityManagerFactory factory) {
		Assert.assertNotNull(factory);
		LOGGER.info(String.format("Entity Manager Factory testing"));
		LOGGER.info(String.format("Persistence Unit [%s]",factory.getPersistenceUnitUtil().toString()));
		factory.getProperties().entrySet().forEach(e->LOGGER.info(String.format("Property [%s]=[%s]", e.getKey(), e.getValue())));
		factory.getMetamodel().getEmbeddables().forEach(e->LOGGER.info(e.getJavaType().getName()));
		EntityManager entityManager = factory.createEntityManager();
		Assert.assertNotNull(entityManager);
		LOGGER.info(String.format("Entity Manager testing"));
		entityManager.getProperties().entrySet().forEach(e->LOGGER.info(String.format("Property [%s]=[%s]", e.getKey(), e.getValue())));

	}

	@Test
	public void testSpringContext() {
		try {
			String applicationName = context.getApplicationName();
			LOGGER.info(String.format("Application [%s] context testing", applicationName));
			LOGGER.info(String.format("Bean definition [%s] context testing", context.getBeanDefinitionCount()));
			for (String name : context.getBeanDefinitionNames()) {
				testBean(name);
			}
			testAnnotate("Service", Service.class);
			testAnnotate("Repository", Repository.class);
			testAnnotate("Entity", Entity.class);

		} catch (Exception ex) {
			ex.printStackTrace();
			Assert.assertNull(ex);
		}
	}

	private void testAnnotate(String string, Class<? extends Annotation> annotation) {
		Map<String, Object> map = context.getBeansWithAnnotation(annotation);
		LOGGER.info(
				String.format("Test Annotation [%s] : [%d] %s beans found", annotation.getName(), map.size(), string));
		map.entrySet().stream().forEach(
				e -> LOGGER.info(String.format("%s Bean [%s] - class [%s]", string, e.getKey(), e.getValue())));
	}

	private void testBean(String name) {
		Object bean = context.getBean(name);
		Assert.assertNotNull(bean);
		LOGGER.info(String.format("Bean Definition id[%s] - class [%s]", name, bean.getClass()));

	}

}
