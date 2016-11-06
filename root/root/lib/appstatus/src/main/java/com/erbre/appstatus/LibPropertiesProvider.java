package com.erbre.appstatus;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.jar.Attributes;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.appstatus.core.IServletContextAware;
import net.sf.appstatus.core.property.impl.AbstractPropertyProvider;

public class LibPropertiesProvider extends AbstractPropertyProvider implements IServletContextAware {

    private static final String BUILD_JDK = "Build-Jdk";

    private static final String META_INF_MANIFEST_MF = "!/META-INF/MANIFEST.MF";

    private static final String WEB_INF_LIB = "/WEB-INF/lib/";

    private static Logger LOGGER = LoggerFactory.getLogger(LibPropertiesProvider.class);

    private ServletContext servletContext;

    private boolean showPath = true;

    public boolean isShowPath() {
        return showPath;
    }

    public void setShowPath(boolean showPath) {
        this.showPath = showPath;
    }

    private boolean classpath = true;

    public boolean isClasspath() {
        return classpath;
    }

    public void setClasspath(boolean classpath) {
        this.classpath = classpath;
    }

    @Override
    public String getCategory() {
        return "Libraries";
    }

    public Map<String, String> getProperties() {
        Map<String, String> prop = new HashMap<String, String>();
        if (this.servletContext != null) {
            try {
                if (isClasspath()) {
                    Enumeration<URL> urls = servletContext.getClassLoader().getResources("/META-INF/MANIFEST.MF");
                    while (urls.hasMoreElements()) {
                        URL url = urls.nextElement();
                        String name = buildJarName(url);
                        String info = buildInfo(url);
                        prop.put(name, info);
                    }
                } else {
                    Set<String> libs = servletContext.getResourcePaths(WEB_INF_LIB);
                    libs.forEach(p -> {
                        String name = p.substring(13);
                        String info = buildInfo(p);
                        prop.put(name, info);
                    });
                }
            } catch (Exception e) {
                LOGGER.error("Error getting resources information from /WEB-INF/lib*", e);
            }
        }
        return prop;
    }

    private String buildJarName(URL url) {
        String path = url.toExternalForm();
        path = path.substring(0, path.length() - 22);
        path = path.substring(path.lastIndexOf('/') + 1);
        return path;
    }

    private String buildInfo(URL url) {
        StringBuilder builder = new StringBuilder();
        String path = url.toExternalForm();
        if (isShowPath()) {
            builder.append("path=[");
            builder.append(path.substring(0, path.length() - 22));
            builder.append("] ");
        }
        InputStream is = null;
        Properties mfProperties = new Properties();
        URLConnection con = null;
        try {
            con = url.openConnection();
            is = con.getInputStream();
            if (is == null) {
                LOGGER.error("Manifest [" + url.toExternalForm() + "] not found");
            } else {
                mfProperties.load(is);
                addInfo(builder, mfProperties, Attributes.Name.IMPLEMENTATION_TITLE.toString());
                addInfo(builder, mfProperties, Attributes.Name.IMPLEMENTATION_VERSION.toString());
                addInfo(builder, mfProperties, Attributes.Name.IMPLEMENTATION_VENDOR.toString());
                addInfo(builder, mfProperties, Attributes.Name.MANIFEST_VERSION.toString());
                addInfo(builder, mfProperties, Attributes.Name.SPECIFICATION_TITLE.toString());
                addInfo(builder, mfProperties, Attributes.Name.SPECIFICATION_VERSION.toString());
                addInfo(builder, mfProperties, Attributes.Name.SPECIFICATION_VENDOR.toString());
                addInfo(builder, mfProperties, BUILD_JDK);
            }
        } catch (Exception e) {
            LOGGER.error("Error getting resources information from " + path + ". no correct manifest?");
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOGGER.warn("Error closing resources to " + path);

                }
            }

        }
        return builder.toString();

    }

    private String buildInfo(String p) {
        StringBuilder builder = new StringBuilder();
        if (isShowPath()) {
            builder.append("path=[");
            builder.append(p);
            builder.append("] ");
        }
        InputStream is = null;
        Properties mfProperties = new Properties();
        String path = p + META_INF_MANIFEST_MF;
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Reading Manifest [" + path + "]");
            }

            is = servletContext.getResourceAsStream(path);
            if (is == null) {
                LOGGER.error("Manifest [" + path + "] not found");
            } else {
                mfProperties.load(is);
                addInfo(builder, mfProperties, Attributes.Name.IMPLEMENTATION_TITLE.toString());
                addInfo(builder, mfProperties, Attributes.Name.IMPLEMENTATION_VERSION.toString());
                addInfo(builder, mfProperties, Attributes.Name.IMPLEMENTATION_VENDOR.toString());
                addInfo(builder, mfProperties, Attributes.Name.MANIFEST_VERSION.toString());
                addInfo(builder, mfProperties, Attributes.Name.SPECIFICATION_TITLE.toString());
                addInfo(builder, mfProperties, Attributes.Name.SPECIFICATION_VERSION.toString());
                addInfo(builder, mfProperties, Attributes.Name.SPECIFICATION_VENDOR.toString());
                addInfo(builder, mfProperties, BUILD_JDK);
            }
        } catch (Exception e) {
            LOGGER.error("Error getting resources information from " + path + ". no correct manifest?");
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOGGER.warn("Error closing resources to " + path);

                }
            }
        }
        return builder.toString();

    }

    private void addInfo(StringBuilder builder, Properties mfProperties, String attribute) {
        String value = mfProperties.getProperty(attribute);
        if (value != null && !value.isEmpty()) {
            builder.append(attribute);
            builder.append("=[");
            builder.append(value);
            builder.append("] ");
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
