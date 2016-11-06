package com.erbre.appstatus.page;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class TemplateEngineImpl implements TemplateEngine {

    private Configuration config;

    @Override
    public String getDefaultEncoding() {
        return config.getDefaultEncoding();
    }

    public TemplateEngineImpl() {
    }
    
    private String templatePath;
    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    private String encoding="UTF-8";

    public void init() throws IOException {
        config = new Configuration(Configuration.VERSION_2_3_24);
        config.setClassLoaderForTemplateLoading(this.getClass().getClassLoader(), templatePath);
        config.setDefaultEncoding(encoding);
        config.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        config.setLogTemplateExceptions(false);
    }

    @Override
    public String applyTemplate(String templateName, Map<String, Object> model) throws IOException {
        try (Writer out = new StringWriter()) {
            Template template = config.getTemplate(templateName);
            template.process(model, out);
            return out.toString();
        } catch (TemplateException | IOException e) {
            throw new IOException(String.format("Unable to apply template[%s]", templateName), e);
        }
    }
}
