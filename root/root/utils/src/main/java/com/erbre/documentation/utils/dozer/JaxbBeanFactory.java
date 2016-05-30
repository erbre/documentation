package com.erbre.documentation.utils.dozer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dozer.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JaxbBeanFactory implements BeanFactory {

    public static final String FAILED_TO_INSTANCIATE_OBJECT_FOR_CLASS_FOR_JAXB_FACTORY_S = "failed to instanciate object for class for jaxb factory [%s]";
    public static final String FAILED_TO_FIND_CLASS_FOR_JAXB_FACTORY_S_CHECK_CLASSPATH = "failed to find class for jaxb factory [%s], check classpath";
    private static final Logger LOGGER = LoggerFactory.getLogger(JaxbBeanFactory.class);

    private class Factory {

        Method m;
        Object source;

        public Object newInstance() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            return m.invoke(source, new Object[0]);
        }

        public String toString() {
            return String.format("Factory<%s,%s>", source.getClass(), m.getName());
        }
    }

    private Map<String, Factory> factories;

    public JaxbBeanFactory() {
        factories = new HashMap<>();
    }

    public void setFactories(List<String> factories) {
        for (String string : factories) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(String.format("init jaxb factory [%s]", string));
            }
            init(string);
        }
    }

    private void init(String factoryName) {
        try {
            Class<?> jaxbFactoryClass = Class.forName(factoryName);
            Object jaxbFactory = jaxbFactoryClass.newInstance();
            Method[] methods = jaxbFactoryClass.getDeclaredMethods();
            for (Method m : methods) {
                initMethod(jaxbFactory, m);
            }
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(
                    String.format(FAILED_TO_FIND_CLASS_FOR_JAXB_FACTORY_S_CHECK_CLASSPATH, factoryName), e);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException(
                    String.format(FAILED_TO_INSTANCIATE_OBJECT_FOR_CLASS_FOR_JAXB_FACTORY_S, factoryName), e);
        }

    }

    private void initMethod(Object jaxbFactory, Method m) {
        if (m.getName().startsWith("create") && m.getParameterCount() == 0) {
            Class<?> type = m.getReturnType();
            Factory factory = new Factory();
            factory.m = m;
            factory.source = jaxbFactory;
            factories.put(type.getName(), factory);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(String.format("init jaxb factory method [%s].[%s]", jaxbFactory.getClass().getName(),
                        m.getName()));
            }
        }
    }

    @Override
    public Object createBean(Object source, Class<?> sourceClass, String targetBeanId) {
        Factory factory = factories.get(targetBeanId);
        if (factory == null) {
            try {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(String.format("Bean creation %s", targetBeanId));
                }
                return Class.forName(targetBeanId).newInstance();
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                if (LOGGER.isWarnEnabled()) {
                    LOGGER.warn(String.format(
                            "failed to create bean by reflection for class [%s], check classpath or bean type",
                            targetBeanId), e);
                }
            }
        } else {
            try {
                return factory.newInstance();
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                if (LOGGER.isWarnEnabled()) {
                    LOGGER.warn(String.format(
                            "failed to to create bean [%s] with jaxb factory [%s], check classpath or bean type",
                            targetBeanId, factory), e);
                }
            }
        }
        return null;
    }

}
