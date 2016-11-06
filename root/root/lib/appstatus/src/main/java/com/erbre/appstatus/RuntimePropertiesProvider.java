package com.erbre.appstatus;

import java.util.HashMap;
import java.util.Map;

import net.sf.appstatus.core.property.impl.AbstractPropertyProvider;

public class RuntimePropertiesProvider extends AbstractPropertyProvider {

    @Override
    public String getCategory() {
        return "Runtime";
    }

    @Override
    public Map<String, String> getProperties() {
        Map<String, String> vmInformation = new HashMap<String, String>();
        vmInformation.put("freeMemory", String.format("[%d] bytes", Runtime.getRuntime().freeMemory()));
        vmInformation.put("usedMemory",
                String.format("[%d] bytes", Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
        vmInformation.put("totalMemory", String.format("[%d] bytes", Runtime.getRuntime().totalMemory()));
        vmInformation.put("maxMemory", String.format("[%d] bytes", Runtime.getRuntime().maxMemory()));
        vmInformation.put("availableProcessors",
                String.format("[%d] processor", Runtime.getRuntime().availableProcessors()));

        return vmInformation;
    }

}
