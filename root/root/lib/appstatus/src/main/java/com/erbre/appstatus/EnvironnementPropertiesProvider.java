package com.erbre.appstatus;

import java.util.Map;

import net.sf.appstatus.core.property.impl.AbstractPropertyProvider;

public class EnvironnementPropertiesProvider extends AbstractPropertyProvider {

    @Override
    public String getCategory() {
        return "EnvProperties";
    }

    @Override
    public Map<String, String> getProperties() {
        return System.getenv();
    }

}
