package com.erbre.appstatus;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.HashMap;
import java.util.Map;

import net.sf.appstatus.core.property.impl.AbstractPropertyProvider;

public class RuntimeMXBeanPropertiesProvider extends AbstractPropertyProvider {

    @Override
    public String getCategory() {
        return "RuntimeMX";
    }

    @Override
    public Map<String, String> getProperties() {
        RuntimeMXBean mxBean = ManagementFactory.getRuntimeMXBean();
        StringBuilder args = new StringBuilder();
        mxBean.getInputArguments().forEach(arg -> args.append(" [").append(arg).append(']'));
        Map<String, String> info = new HashMap<String, String>();
        info.put("args", args.toString());
        info.put("bootClassPath", mxBean.getBootClassPath());
        info.put("libraryPath", mxBean.getLibraryPath());
        info.put("managementSpecVersion", mxBean.getManagementSpecVersion());
        info.put("name", mxBean.getName());
        info.put("SpecName", mxBean.getSpecName());
        info.put("SpecVendor", mxBean.getSpecVendor());
        info.put("vmName", mxBean.getVmName());
        info.put("vmVendor", mxBean.getVmVendor());
        info.put("vmVersion", mxBean.getVmVersion());
        info.put("startTime", String.format("[%tc]", mxBean.getStartTime()));
        info.put("upTime", String.format("[%s]", mxBean.getUptime()));
        info.put("bootClassPathSupported", String.format("[%s]", mxBean.isBootClassPathSupported()));

        return info;
    }

}
