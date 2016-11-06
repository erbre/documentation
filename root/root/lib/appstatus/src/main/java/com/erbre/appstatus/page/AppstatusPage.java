package com.erbre.appstatus.page;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.appstatus.web.StatusWebHandler;
import net.sf.appstatus.web.pages.AbstractPage;

public abstract class AppstatusPage extends AbstractPage {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppstatusPage.class.getName());

    public AppstatusPage() {
        template = this.getClass().getSimpleName() + "-template.html";
        LOGGER.info(String.format("Template for Page[%s] is [%s]", this.getClass().getName(), template));
    }

    @Inject
    private TemplateEngine templateEngine;

    private String template;

    public void setTemplate(String templateFile) {
        this.template = templateFile;
    }

    @Override
    public void doGet(StatusWebHandler webHandler, HttpServletRequest req, HttpServletResponse resp)
            throws UnsupportedEncodingException, IOException {
        setup(resp, "text/html");
//        <link href="path/to/jquery.treetable.css" rel="stylesheet" type="text/css" />
//        <script src="path/to/jquery.treetable.js"></script>
        try (ServletOutputStream os = resp.getOutputStream()) {
            Map<String, String> valuesMap = new HashMap<String, String>();
            Map<String, Object> model = getModel(req);
            model.put("page", getId());
            model.put("pageTitle", getName());
            LOGGER.info(String.format("Calling template[%s]", template));
            valuesMap.put("content", templateEngine.applyTemplate(template, model));
            os.write(getPage(webHandler, valuesMap).getBytes(templateEngine.getDefaultEncoding()));
        }
    }

    protected abstract Map<String, Object> getModel(HttpServletRequest req);

    public void doPost(StatusWebHandler webHandler, HttpServletRequest req, HttpServletResponse resp) {

    }

}
