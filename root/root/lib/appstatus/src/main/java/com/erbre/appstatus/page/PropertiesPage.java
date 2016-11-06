package com.erbre.appstatus.page;

import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.jar.Attributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesPage extends AppstatusPage {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesPage.class.getName());

    private static final String ENV_PROPERTIES = "ENV";
    private static final String SYSTEM_PROPERTIES = "SYS";
    private static final String SERVLET_PROPERTIES = "SERVLET";
    private static final String RUNTIME_PROPERTIES = "RUNTIME";
    private static final String MANIFEST_PROPERTIES = "MANIFEST";
    private static final String OS_PROPERTIES = "OS";
    private static final String BUILD_JDK = "Build-Jdk";

    private static final String META_INF_MANIFEST = "/META-INF/MANIFEST.MF";

    public String getId() {
        return "Properties";
    }

    public String getName() {
        return "Properties";
    }

    protected Map<String, Object> getModel(HttpServletRequest req) {
        Map<String, Object> model = new HashMap<String, Object>();
        String propertiesToFind = req.getParameter("properties");
        LOGGER.debug(String.format("Request for looking properties[%s]", propertiesToFind));
        if (propertiesToFind != null) {
            switch (propertiesToFind) {
            case ENV_PROPERTIES:
                model.put("properties", buildEnvProperties(model));
                break;
            case SYSTEM_PROPERTIES:
                model.put("properties", buildSystemProperties(model));
                break;
            case MANIFEST_PROPERTIES:
                model.put("properties", buildManifestProperties(model, req.getServletContext()));
                break;
            case SERVLET_PROPERTIES:
                model.put("properties", buildServletProperties(model, req.getServletContext()));
                break;
            case OS_PROPERTIES:
                model.put("properties", buildOsProperties(model));
                break;
            case RUNTIME_PROPERTIES:
                model.put("properties", buildRuntimeProperties(model));
                break;
            default:
                model.put("properties", buildProperties(model, propertiesToFind, req.getServletContext()));
            }

        }
        return model;
    }

    private Object buildOsProperties(Map<String, Object> model) {
        model.put("path", "OperatingSystem");
        model.put("context", "MXBean");
        OperatingSystemMXBean mxBean = ManagementFactory.getOperatingSystemMXBean();
        Map<String, Object> info = new HashMap<>();
        info.put("arch",mxBean.getArch());
        info.put("name",mxBean.getName());
        info.put("version",mxBean.getVersion());
        info.put("available.processors",mxBean.getAvailableProcessors());
        info.put("system.load.average",mxBean.getSystemLoadAverage());
        return info;
    }
    private Object buildRuntimeProperties(Map<String, Object> model) {
        model.put("path", "Runtime");
        model.put("context", "MXBean");
        RuntimeMXBean mxBean = ManagementFactory.getRuntimeMXBean();
        StringBuilder args = new StringBuilder();
        mxBean.getInputArguments().forEach(arg -> args.append(" [").append(arg).append(']'));
        Map<String, Object> info = new HashMap<>();
         info.put("args", args.toString());
        info.put("bootClassPath", mxBean.getBootClassPath());
        info.put("libraryPath", mxBean.getLibraryPath());
        info.put("managementSpecVersion", mxBean.getManagementSpecVersion());
        info.put("name", mxBean.getName());
        info.put("SpecName", mxBean.getSpecName());
        info.put("SpecVendor", mxBean.getSpecVendor());
        info.put("vmName", mxBean.getVmName());
        info.put("vmVendor", mxBean.getVmVendor());
        info.put("vmVersion", mxBean.getVmVersion());
        info.put("startTime", String.format("[%tc]", mxBean.getStartTime()));
        info.put("upTime", String.format("[%s]", mxBean.getUptime()));
        info.put("bootClassPathSupported", String.format("[%s]", mxBean.isBootClassPathSupported()));
        info.put("freeMemory", String.format("[%d] bytes", Runtime.getRuntime().freeMemory()));
        info.put("usedMemory",
                String.format("[%d] bytes", Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
        info.put("totalMemory", String.format("[%d] bytes", Runtime.getRuntime().totalMemory()));
        info.put("maxMemory", String.format("[%d] bytes", Runtime.getRuntime().maxMemory()));
        info.put("availableProcessors",
                String.format("[%d] processor", Runtime.getRuntime().availableProcessors()));
        return info;
    }
    private Object buildManifestProperties(Map<String, Object> model, ServletContext servletContext) {
        model.put("path", META_INF_MANIFEST);
        model.put("context", "Servlet");
        Map<String, Object> prop = new TreeMap<String, Object>();
        InputStream is = null;
        Properties properties = null;
        if (servletContext != null) {
            try {
                is = servletContext.getResourceAsStream(META_INF_MANIFEST);
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("Reading manifest information from [" + META_INF_MANIFEST + "]");
                }
                if (is == null) {
                    model.put("warn", "no manifest found");
                    return null;
                } else {
                    properties = new Properties();
                    properties.load(is);
                    if (properties.isEmpty()) {
                        model.put("warn", "empty manifest found");
                        return null;
                    } else {
                        properties.forEach((k, v) -> prop.put(k.toString(), v));
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Error getting manifest information from [" + META_INF_MANIFEST + "]", e);
                model.put("error", String.format("Error in getting manifest information [%s]", e.getMessage()));
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        LOGGER.warn("Error closing resource manifest from [" + META_INF_MANIFEST + "]", e);
                        model.put("warn", String.format("Error in closing resource manifest [%s]", e.getMessage()));
                    }
                }
            }
        }else {
            return null;
        }
        return prop;

    }

    private Object buildServletProperties(Map<String, Object> model, ServletContext servletContext) {
        model.put("path", "Servlet");
        model.put("context", "Servlet");
        Map<String, Object> prop = new TreeMap<String, Object>();
        prop.put("context.path", servletContext.getContextPath());
        prop.put("server.info", servletContext.getServerInfo());
        prop.put("servlet.context.name", servletContext.getServletContextName());
        prop.put("virtual.server.name", servletContext.getVirtualServerName());
        prop.put("version", "v" + servletContext.getMajorVersion() + "." + servletContext.getMinorVersion());
        prop.put("version.effective",
                "v" + servletContext.getEffectiveMajorVersion() + "." + servletContext.getEffectiveMinorVersion());
        Enumeration<String> attNames = servletContext.getAttributeNames();
        while (attNames.hasMoreElements()) {
            String attName = attNames.nextElement();
            prop.put("servlet.attribute." + attName, servletContext.getAttribute(attName));
        }
        Enumeration<String> paramNames = servletContext.getInitParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            prop.put("servlet.init.param." + paramName, servletContext.getInitParameter(paramName));
        }
        servletContext.getServletRegistrations().forEach((k, v) -> {
            String prefix = "servlet.registration." + k;
            prop.put(prefix + ".name", v.getName());
            prop.put(prefix + ".className", v.getClassName());
            prop.put(prefix + ".runAsRole", v.getRunAsRole());
            StringBuilder mappings = new StringBuilder();
            v.getMappings().forEach(m -> mappings.append("[").append(m).append("] "));
            prop.put(prefix + ".mappings", mappings.toString());
            StringBuilder parameters = new StringBuilder();
            v.getInitParameters()
                    .forEach((pk, pv) -> parameters.append("[").append(pk).append("]=[").append(pv).append("]  "));
            prop.put(prefix + ".init.param", parameters.toString());
        });
        return prop;
    }

    private Object buildProperties(Map<String, Object> model, String propertiesToFind, ServletContext servletContext) {
        Map<String, Object> retour = new TreeMap<>();
        String path;
        if (propertiesToFind.startsWith("/")) {
            path = propertiesToFind;
        } else {
            path = "/" + propertiesToFind;
        }
        model.put("path", path);
        InputStream is = null;
        try {
            model.put("context", "servlet");
            is = servletContext.getClassLoader().getResourceAsStream(path);
            if (is == null) {
                model.put("context", "classpath");
                is = servletContext.getResourceAsStream(path);
            } else {
            }
            if (is != null) {
                Properties properties = new Properties();
                properties.load(is);
                properties.forEach((k, v) -> retour.put(k.toString(), v.toString()));
            } else {
                model.put("warn", String.format("Properties File [%s] not found", path));
                return null;
            }
        } catch (Exception e) {
            LOGGER.error(String.format("Error in getting resource stream [%s]", path), e);
            model.put("error", e.getMessage());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOGGER.warn(String.format("Error in closing resource stream [%s] ", path), e);
                    model.put("warn", String.format("Error in closing resource stream [%s] ", e.getMessage()));

                }
            }
        }
        return retour;

    }

    private Object buildSystemProperties(Map<String, Object> model) {
        model.put("path", "System");
        model.put("context", "System");
        Map<String, Object> properties = new TreeMap<>();
        System.getProperties().forEach((k, v) -> properties.put(k.toString(), v));
        return properties;
    }

    private Object buildEnvProperties(Map<String, Object> model) {
        model.put("path", "Environnement");
        model.put("context", "System");
        Map<String, Object> properties = new TreeMap<>();
        System.getenv().forEach((k, v) -> properties.put(k, v));
        return properties;

    }

}
