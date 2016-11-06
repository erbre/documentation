package appstatus;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.erbre.appstatus.ManifestReader;

public class ManifestReaderTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ManifestReaderTest.class.getName());

    @Test
    public void testReadStandardManifest() {
        File file = new File("src/test/resources/manifest/cxf-rt-wsdl-3.0.5.jar.META-INF.MANIFEST.MF");
        try (FileInputStream fis = new FileInputStream(file)) {
            ManifestReader reader = new ManifestReader();

            reader.load(fis);

            reader.getMap().forEach((k, v) -> System.out.println("[" + k + "]=[" + v + "]"));
            Assert.assertEquals(
                    "javax.annotation,javax.wsdl,javax.wsdl.extensions,javax.wsdl.extensions.mime,javax.wsdl.extensions.schema,javax.wsdl.extensions.soap,javax.wsdl.extensions.soap12,javax.wsdl.factory,javax.wsdl.xml,javax.xml.bind,javax.xml.bind.annotation,javax.xml.bind.annotation.adapters,javax.xml.namespace;resolution:=optional,javax.xml.stream;resolution:=optional,javax.xml.stream.util;resolution:=optional,javax.xml.transform;resolution:=optional,javax.xml.transform.dom;resolution:=optional,org.apache.cxf;version=\"[3.0,4)\",org.apache.cxf.annotations;version=\"[3.0,4)\",org.apache.cxf.binding;version=\"[3.0,4)\",org.apache.cxf.catalog;version=\"[3.0,4)\",org.apache.cxf.common.classloader;version=\"[3.0,4)\",org.apache.cxf.common.i18n;version=\"[3.0,4)\",org.apache.cxf.common.injection;version=\"[3.0,4)\",org.apache.cxf.common.jaxb;version=\"[3.0,4)\",org.apache.cxf.common.logging;version=\"[3.0,4)\",org.apache.cxf.common.util;version=\"[3.0,4)\",org.apache.cxf.common.xmlschema;version=\"[3.0,4)\",org.apache.cxf.configuration;version=\"[3.0,4)\",org.apache.cxf.databinding;version=\"[3.0,4)\",org.apache.cxf.databinding.source.mime;version=\"[3.0,4)\",org.apache.cxf.endpoint;version=\"[3.0,4)\",org.apache.cxf.feature;version=\"[3.0,4)\",org.apache.cxf.helpers;version=\"[3.0,4)\",org.apache.cxf.interceptor;version=\"[3.0,4)\",org.apache.cxf.message;version=\"[3.0,4)\",org.apache.cxf.phase;version=\"[3.0,4)\",org.apache.cxf.resource;version=\"[3.0,4)\",org.apache.cxf.service;version=\"[3.0,4)\",org.apache.cxf.service.factory;version=\"[3.0,4)\",org.apache.cxf.service.invoker;version=\"[3.0,4)\",org.apache.cxf.service.model;version=\"[3.0,4)\",org.apache.cxf.staxutils;version=\"[3.0,4)\",org.apache.cxf.staxutils.transform;version=\"[3.0,4)\",org.apache.cxf.transport;version=\"[3.0,4)\",org.apache.cxf.ws.addressing;version=\"[3.0,4)\",org.apache.ws.commons.schema;version=\"[2.2,3)\",org.apache.ws.commons.schema.constants;version=\"[2.2,3)\",org.apache.ws.commons.schema.extensions;version=\"[2.2,3)\",org.apache.ws.commons.schema.resolver;version=\"[2.2,3)\",org.apache.ws.commons.schema.utils;version=\"[2.2,3)\",org.apache.xerces.dom;resolution:=optional,org.apache.xerces.xs;resolution:=optional,org.w3c.dom;resolution:=optional,org.w3c.dom.ls;resolution:=optional,org.xml.sax;resolution:=optional",
                    reader.getMap().get("Import-Package"));
            Assert.assertEquals(
                    "org.apache.cxf.wsdl;version=\"3.0.5\",org.apache.cxf.wsdl.binding;version=\"3.0.5\",org.apache.cxf.wsdl.interceptors;version=\"3.0.5\",org.apache.cxf.wsdl.service.factory;version=\"3.0.5\",org.apache.cxf.wsdl11;version=\"3.0.5\"",
                    reader.getMap().get("Export-Package"));
            Assert.assertEquals("3.0.5", reader.getMap().get("Bundle-Version"));
        } catch (IOException e) {
            LOGGER.error("Failed to read Manifest from file [" + file.getAbsolutePath() + "]", e);
            Assert.assertNull(e);
        }
    }

    @Test
    public void testReadAllManifest() {
        File dir = new File("src/test/resources/manifest/");
        for (File file : dir.listFiles()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                ManifestReader reader = new ManifestReader();
                reader.load(fis);
                Assert.assertTrue(reader.getMap().size() > 0);
            } catch (IOException e) {
                LOGGER.error("Failed to read Manifest from file [" + file.getAbsolutePath() + "]", e);
                Assert.assertNull(e);
            }
        }
    }
}
