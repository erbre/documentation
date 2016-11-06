package com.erbre.appstatus;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.appstatus.core.IServletContextAware;
import net.sf.appstatus.core.property.impl.AbstractPropertyProvider;

public class PropertiesFileProvider extends AbstractPropertyProvider implements IServletContextAware {

    private static final String APPLICATION_PROPERTIES = "application.properties";

    private static Logger LOGGER = LoggerFactory.getLogger(PropertiesFileProvider.class);

    private String propertyFileName = APPLICATION_PROPERTIES;

    private ServletContext servletContext;

    public String getPropertyFileName() {
        return propertyFileName;
    }

    private boolean classpath = true;

    public boolean isClasspath() {
        return classpath;
    }

    public void setClasspath(boolean classpath) {
        this.classpath = classpath;
    }

    public void setPropertyFileName(String propertyFileName) {
        this.propertyFileName = propertyFileName;
    }

    @Override
    public String getCategory() {
        return String.format("Properties [%s]",getPropertyFileName());
    }

    public Map<String, String> getProperties() {
        Map<String, String> prop = new HashMap<String, String>();
        InputStream is = null;
        if (this.servletContext != null) {
            try {
                if (isClasspath()) {
                    is = servletContext.getClassLoader().getResourceAsStream("/" + getPropertyFileName());
                } else {
                    is = servletContext.getResourceAsStream("/" + getPropertyFileName());
                }
                if (is != null) {
                    Properties properties = new Properties();
                    properties.load(is);
                    properties.forEach((k, v) -> prop.put(k.toString(), v.toString()));
                } else {
                    LOGGER.warn(String.format(
                            "No properties file [%s] found "
                                    + (isClasspath() ? " in classpath " : " in servlet context path "),
                            getPropertyFileName()));
                }
            } catch (Exception e) {
                LOGGER.error(String.format(
                        "Error in getting properties file [%s] "
                                + (isClasspath() ? " in classpath " : " in servlet context path "),
                        getPropertyFileName()), e);
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        LOGGER.warn(String.format(
                                "Error in closing resource stream [%s] "
                                        + (isClasspath() ? " in classpath " : " in servlet context path "),
                                getPropertyFileName()), e);

                    }
                }
            }
        }
        return prop;

    }

    /**
     * {@inheritDoc}
     * 
     * @see net.sf.appstatus.core.IServletContextAware#setServletContext(javax.servlet.ServletContext)
     */
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
