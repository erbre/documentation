package com.erbre.appstatus.page;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ModelBuilder {

    private Map<String, Object> root;
    public Map<String, Object> getRoot() {
        return root;
    }
    private List<Object> warnings;
    private List<Object> errors;

    public ModelBuilder() {
        root = new TreeMap<>();
        warnings = new ArrayList<>();
        errors = new ArrayList<>();
    }

    public Map<String, Object> finalizeRoot() {
        if (!warnings.isEmpty()) {
            root.put("warning", warnings);
        }
        if (!errors.isEmpty()) {
            root.put("error", errors);
        }
        return root;
    }
    
    public void error(Object msg) {
        errors.add(msg);
    }
    public void warn(Object msg) {
        warnings.add(msg);
    }

}
