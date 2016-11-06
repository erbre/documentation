package com.erbre.documentation.web.controller.bean;

import java.io.Serializable;
import java.util.Map;

public class TitleMap implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    Map<String, Object> map;

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String title;
}
