package com.erbre.appstatus;

import java.util.HashMap;
import java.util.Map;

import net.sf.appstatus.core.property.impl.AbstractPropertyProvider;

public class SystemPropertiesProvider extends AbstractPropertyProvider {

    @Override
    public String getCategory() {
        return "SystemProperties";
    }

    @Override
    public Map<String, String> getProperties() {
        Map<String, String> sys = new HashMap<>();
        System.getProperties().forEach((k, v) -> sys.put(k.toString(), v == null ? "" : v.toString()));
        return sys;
    }

}
