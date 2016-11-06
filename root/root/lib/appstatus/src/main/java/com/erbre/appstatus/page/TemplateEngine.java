package com.erbre.appstatus.page;

import java.io.IOException;
import java.util.Map;

public interface TemplateEngine {

    String applyTemplate(String templateName, Map<String, Object> model) throws IOException;

    String getDefaultEncoding();

}
