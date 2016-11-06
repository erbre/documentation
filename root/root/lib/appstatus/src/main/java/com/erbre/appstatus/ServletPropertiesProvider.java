package com.erbre.appstatus;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.jar.Attributes;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.appstatus.core.IServletContextAware;
import net.sf.appstatus.core.property.impl.AbstractPropertyProvider;

public class ServletPropertiesProvider extends AbstractPropertyProvider implements IServletContextAware {

    private static Logger LOGGER = LoggerFactory.getLogger(ServletPropertiesProvider.class);

    private ServletContext servletContext;

    private boolean showAttributes = false;

    public boolean isShowAttributes() {
        return showAttributes;
    }

    public void setShowAttributes(boolean showAttributes) {
        this.showAttributes = showAttributes;
    }

    @Override
    public String getCategory() {
        return "Servlet";
    }

    public Map<String, String> getProperties() {
        Map<String, String> prop = new HashMap<String, String>();
        prop.put("context.path", servletContext.getContextPath());
        prop.put("server.info", servletContext.getServerInfo());
        prop.put("servlet.context.name", servletContext.getServletContextName());
        prop.put("virtual.server.name", servletContext.getVirtualServerName());
        prop.put("version", "v" + servletContext.getMajorVersion() + "." + servletContext.getMinorVersion());
        prop.put("version.effective",
                "v" + servletContext.getEffectiveMajorVersion() + "." + servletContext.getEffectiveMinorVersion());
        if (isShowAttributes()) {
            StringBuilder attributes = new StringBuilder();
            Enumeration<String> attNames = servletContext.getAttributeNames();
            while (attNames.hasMoreElements()) {
                String attName = attNames.nextElement();
                attributes.append("[");
                attributes.append(attName);
                attributes.append("]=[");
                attributes.append(servletContext.getAttribute(attName));
                attributes.append("] ");
            }
            prop.put("attributes", attributes.toString());
        }
        StringBuilder params = new StringBuilder();
        Enumeration<String> paramNames = servletContext.getInitParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            params.append("[");
            params.append(paramName);
            params.append("]=[");
            params.append(servletContext.getInitParameter(paramName));
            params.append("] ");
        }
        prop.put("init.parameters", params.toString());
        servletContext.getServletRegistrations().forEach((k, v) -> {
            prop.put("servlet." + k + ".name", v.getName());
            prop.put("servlet." + k + ".className", v.getClassName());
            prop.put("servlet." + k + ".runAsRole", v.getRunAsRole());
            StringBuilder mappings = new StringBuilder();
            v.getMappings().forEach(m -> mappings.append("[").append(m).append("] "));
            prop.put("servlet." + k + ".mappings", mappings.toString());
            StringBuilder parameters = new StringBuilder();
            v.getInitParameters()
                    .forEach((pk, pv) -> parameters.append("[").append(pk).append("]=[").append(pv).append("]  "));
            prop.put("servlet." + k + ".initParameters", parameters.toString());
        });
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
