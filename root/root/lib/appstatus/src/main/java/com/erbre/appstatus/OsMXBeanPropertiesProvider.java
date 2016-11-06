package com.erbre.appstatus;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.HashMap;
import java.util.Map;

import net.sf.appstatus.core.property.impl.AbstractPropertyProvider;

public class OsMXBeanPropertiesProvider extends AbstractPropertyProvider {

    @Override
    public String getCategory() {
        return "OperatingSystemMXBean";
    }

    @Override
    public Map<String, String> getProperties() {
        OperatingSystemMXBean mxBean = ManagementFactory.getOperatingSystemMXBean();
        Map<String, String> info = new HashMap<String, String>();
        info.put("OS", String.format("Arch[%s] Name[%s] Version[%s]", mxBean.getArch(), mxBean.getName(),
                mxBean.getVersion()));
        info.put("Processors", String.format("[%s] processors - load average[%s]", mxBean.getAvailableProcessors(),
                mxBean.getSystemLoadAverage()));


        return info;
    }

}
