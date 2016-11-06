package com.erbre.appstatus.page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.erbre.appstatus.aop.model.ModelReader;

public class ModelPage extends AppstatusPage {

    @Inject
    private ModelReader reader;

    public void setReader(ModelReader reader) {
        this.reader = reader;
    }

    public String getId() {
        return "Model";
    }

    public String getName() {
        return "Mesure Model";
    }

    protected Map<String, Object> getModel(HttpServletRequest req) {
        Map<String, Object> info = new HashMap<String, Object>();
        List<Object> trees = new ArrayList<>();
        reader.getTrees().forEach((k, v) -> {
            trees.add(v);
        });
        info.put("trees", trees);
        List<Object> objects = new ArrayList<>();
        reader.getObjects().forEach((k, v) -> {
            objects.add(v);
        });
        info.put("objects", objects);
        return info;
    }

}
