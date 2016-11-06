package com.erbre.appstatus.page;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.jar.Attributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.erbre.appstatus.ManifestReader;

public class LibrariesPage extends AppstatusPage {

    private static final Logger LOGGER = LoggerFactory.getLogger(LibrariesPage.class.getName());

    private static final String BUILD_JDK = "Build-Jdk";

    private static final String META_INF_MANIFEST_MF = "!/META-INF/MANIFEST.MF";

    private static final String WEB_INF_LIB = "/WEB-INF/lib/";

    private static final String NULL_VALUE = "null";

    private boolean classpathSearch = true;
    private boolean servletSearch = true;

    public boolean isClasspathSearch() {
        return classpathSearch;
    }

    public void setClasspathSearch(boolean classpathSearch) {
        this.classpathSearch = classpathSearch;
    }

    public boolean isServletSearch() {
        return servletSearch;
    }

    public void setServletSearch(boolean servletSearch) {
        this.servletSearch = servletSearch;
    }

    public String getId() {
        return "Libraries";
    }

    public String getName() {
        return "Libraries";
    }

    @Override
    protected Map<String, Object> getModel(HttpServletRequest req) {
        ServletContext servletContext = req.getServletContext();
        String showDetail = req.getParameter("id");
        ModelBuilder builder = new ModelBuilder();
        if (servletContext != null) {
            try {
                Map<String, Object> libraries = new HashMap<>();
                if (isClasspathSearch()) {
                    Enumeration<URL> urls = servletContext.getClassLoader().getResources("/META-INF/MANIFEST.MF");
                    while (urls.hasMoreElements()) {
                        URL url = urls.nextElement();
                        String name = buildJarName(url);
                        MFLoader loader = new ClassLoaderMFLoader(url, name);
                        libraries.put(name, buildInfo(builder, loader, showDetail));
                    }
                } else if (isServletSearch()) {
                    Set<String> libs = servletContext.getResourcePaths(WEB_INF_LIB);
                    libs.forEach(p -> {
                        String name = p.substring(13);
                        MFLoader loader = new ServletContextMFLoader(servletContext, name);
                        libraries.put(name, buildInfo(builder, loader, showDetail));
                    });
                }
                builder.getRoot().put("libraires", libraries);
            } catch (Exception e) {
                LOGGER.error("Error getting resources information from /WEB-INF/lib*", e);
                builder.error(String.format("Error in library search [%s]", e));
            }
        } else {
            builder.error("No servlet context");
        }
        return builder.finalizeRoot();
    }

    private String buildJarName(URL url) {
        String path = url.toExternalForm();
        path = path.substring(0, path.length() - 22);
        path = path.substring(path.lastIndexOf('/') + 1);
        return path;
    }

    private Object buildInfo(ModelBuilder builder, MFLoader loader, String showDetail) {
        Map<String, Object> map = new HashMap<>();
        String path = loader.getPath();
        String name = loader.getName();
        map.put("path", path);
        map.put("loadmode", loader.getMode());
        InputStream is = null;
        ManifestReader mfReader = new ManifestReader();
        try {
            is = loader.getInputStream();
            if (is == null) {
                String msg = String.format("Library [%s] load from classloader : No Manifest found in path [%s]", name,
                        path);
                LOGGER.error(msg);
                builder.error(msg);
            } else {
                mfReader.load(is);
                loadManifest(map, mfReader.getMap());
                if (name.equals(showDetail)) {
                    Map<String, Object> manifestDetails = new TreeMap<>();
                    mfReader.getMap().forEach((k, v) -> {
                        manifestDetails.put(k.toString(), v == null ? "" : v.toString());
                    });
                    Map<String, Object> details = new HashMap<>();
                    details.put("name", name);
                    details.put("mf", manifestDetails);
                    builder.getRoot().put("details", details);
                }
            }
        } catch (Exception e) {
            LOGGER.error(String.format("Error getting resources information from [%s]. no correct manifest?", path), e);
            builder.error(String.format("Error getting resources information from [%s]. no correct manifest? : [%s]",
                    path, e.getMessage()));
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOGGER.warn(String.format("Error closing resources to [%s]", path), e);
                    builder.warn(String.format("Error closing resources to [%s] : [%s]", path, e.getMessage()));
                }
            }
        }
        return map;

    }

    private void loadManifest(Map<String, Object> map, Map<String, String> mfProperties) throws IOException {
        addInfo(map, mfProperties, Attributes.Name.IMPLEMENTATION_TITLE.toString());
        addInfo(map, mfProperties, Attributes.Name.IMPLEMENTATION_VERSION.toString());
        addInfo(map, mfProperties, Attributes.Name.IMPLEMENTATION_VENDOR.toString());
        addInfo(map, mfProperties, Attributes.Name.MANIFEST_VERSION.toString());
        addInfo(map, mfProperties, Attributes.Name.SPECIFICATION_TITLE.toString());
        addInfo(map, mfProperties, Attributes.Name.SPECIFICATION_VERSION.toString());
        addInfo(map, mfProperties, Attributes.Name.SPECIFICATION_VENDOR.toString());
        addInfo(map, mfProperties, BUILD_JDK);
    }

    private interface MFLoader {
        public String getPath();

        public InputStream getInputStream() throws IOException;

        public String getMode();

        public String getName();
    }

    private class ClassLoaderMFLoader implements MFLoader {
        private URL url;

        public ClassLoaderMFLoader(java.net.URL url, String name) {
            super();
            this.url = url;
            this.name = name;
        }

        private String name;

        @Override
        public String getPath() {
            return url.toExternalForm();
        }

        @Override
        public InputStream getInputStream() throws IOException {
            URLConnection con = url.openConnection();
            return con.getInputStream();
        }

        @Override
        public String getMode() {
            return "classLoader";
        }

        @Override
        public String getName() {
            return name;
        }
    }

    private class ServletContextMFLoader implements MFLoader {
        private ServletContext servletContext;

        public ServletContextMFLoader(ServletContext servletContext, String name) {
            super();
            this.servletContext = servletContext;
            this.name = name;
        }

        private String name;

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getPath() {
            return WEB_INF_LIB + name + META_INF_MANIFEST_MF;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return servletContext.getResourceAsStream(name + META_INF_MANIFEST_MF);
        }

        @Override
        public String getMode() {
            return "servletContext";
        }
    }

    private void addInfo(Map<String, Object> map, Map<String, String> mfProperties, String attribute) {
        String value = mfProperties.get(attribute);
        if (value != null && !value.isEmpty()) {
            map.put(attribute.replace("-", ""), value);
        } else {
            map.put(attribute.replace("-", ""), NULL_VALUE);
        }

    }

}
