package com.erbre.appstatus;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.jar.Attributes;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.appstatus.core.IServletContextAware;
import net.sf.appstatus.core.property.impl.AbstractPropertyProvider;

public class ManifestPropertiesProvider extends AbstractPropertyProvider implements IServletContextAware {
    private static final String BUILD_JDK = "Build-Jdk";

    private static final String META_INF_MANIFEST = "/META-INF/MANIFEST.MF";

    private static Logger LOGGER = LoggerFactory.getLogger(ManifestPropertiesProvider.class);

    private ServletContext servletContext;
    
    

    @Override
    public String getCategory() {
        return "Manifest";
    }

    public Map<String, String> getProperties() {
        Map<String, String> prop = new HashMap<String, String>();
        InputStream is = null;
        Properties properties = null;
        if (this.servletContext != null) {
            try {
                is = servletContext.getResourceAsStream(META_INF_MANIFEST);
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("Reading manifest information from [" + META_INF_MANIFEST + "]");
                }
                if (is == null) {
                    prop.put("no manifest found", "");
                } else {
                    properties = new Properties();
                    properties.load(is);
                    if (properties.isEmpty()) {
                        prop.put("empty manifest found", "");
                    } else {
                        addProperty(prop, properties, Attributes.Name.IMPLEMENTATION_TITLE.toString());
                        addProperty(prop, properties, Attributes.Name.IMPLEMENTATION_VERSION.toString());
                        addProperty(prop, properties, Attributes.Name.IMPLEMENTATION_VENDOR.toString());
                        addProperty(prop, properties, Attributes.Name.MANIFEST_VERSION.toString());
                        addProperty(prop, properties, Attributes.Name.SPECIFICATION_TITLE.toString());
                        addProperty(prop, properties, Attributes.Name.SPECIFICATION_VERSION.toString());
                        addProperty(prop, properties, Attributes.Name.SPECIFICATION_VENDOR.toString());
                        addProperty(prop, properties, BUILD_JDK);
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Error getting manifest information from [" + META_INF_MANIFEST + "]", e);
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        LOGGER.warn("Error closing ressource manifest information from [" + META_INF_MANIFEST + "]", e);
                    }
                }
            }
        }
        return prop;
    }

    private void addProperty(Map<String, String> prop, Properties properties, String string) {
        Object value = properties.getProperty(string);
        if (value != null) {
            prop.put(string, value.toString());
        }

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
