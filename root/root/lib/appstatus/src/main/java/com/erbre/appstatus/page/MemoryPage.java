package com.erbre.appstatus.page;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.erbre.appstatus.Utils;

public class MemoryPage extends AppstatusPage {

    private static final long UNDEFINED_VALUE = -1l;

    public String getId() {
        return "MemoryDetails";
    }

    public String getName() {
        return "Memory Details";
    }

    protected Map<String, Object> getModel(HttpServletRequest req) {
        Map<String, Object> info = new HashMap<String, Object>();
        List<Object> managers = new ArrayList<>();
        ManagementFactory.getMemoryManagerMXBeans().forEach(m -> {
            Map<String, Object> managerInfo = new HashMap<String, Object>();
            managerInfo.put("name", m.getName());
            List<String> managedList = new ArrayList<>();
            for (String p : m.getMemoryPoolNames()) {
                managedList.add(p);
            }
            managerInfo.put("pools", managedList);
            managers.add(managerInfo);
        });
        info.put("managers", managers);

        List<Object> pools = new ArrayList<>();
        ManagementFactory.getMemoryPoolMXBeans().forEach(m -> {
            pools.add(infoPool(m));
        });
        info.put("pools", pools);

        MemoryMXBean mxBean = ManagementFactory.getMemoryMXBean();
        mapUsage(info, "heap", mxBean.getHeapMemoryUsage());
        mapUsage(info, "nonHeap", mxBean.getNonHeapMemoryUsage());
        info.put("objectPendingFinalization", mxBean.getObjectPendingFinalizationCount());

        List<GarbageCollectorMXBean> collectors = ManagementFactory.getGarbageCollectorMXBeans();
        List<Object> garbageCollectors = new ArrayList<>();
        collectors.forEach(c -> {
            garbageCollectors.add(infoGarbageCollector(c));
        });

        info.put("garbageCollectors", garbageCollectors);

        info.put("classloading", infoClassLoading(ManagementFactory.getClassLoadingMXBean()));
        return info;
    }

    private Object infoClassLoading(ClassLoadingMXBean classLoadingMXBean) {
        Map<String, Object> map = new HashMap<>();
        map.put("loadedClassCount", classLoadingMXBean.getLoadedClassCount());
        map.put("totalLoadedClassCount", classLoadingMXBean.getTotalLoadedClassCount());
        map.put("unloadedClassCount", classLoadingMXBean.getUnloadedClassCount());
        map.put("verbose", classLoadingMXBean.isVerbose());
        return map;
    }

    private Object infoGarbageCollector(GarbageCollectorMXBean c) {
        Map<String, Object> map = new HashMap<>();
        map.put("collectionCount", c.getCollectionCount());
        map.put("collectionTime", Utils.formatMilliSeconds(c.getCollectionTime()));
        map.put("name", c.getName());
        List<String> managedList = new ArrayList<>();
        for (String p : c.getMemoryPoolNames()) {
            managedList.add(p);
        }
        map.put("pools", managedList);
        return map;
    }

    private Map<String, Object> infoPool(MemoryPoolMXBean m) {
        Map<String, Object> map = new HashMap<>();
        String name = m.getName();
        map.put("name", name);
        map.put("type", m.getType().name());
        List<String> managerList = new ArrayList<>();
        for (String manager : m.getMemoryManagerNames()) {
            managerList.add(manager);
        }
        map.put("managers", managerList);
        MemoryUsage collectionUsage = m.getCollectionUsage();
        if (collectionUsage != null) {
            map.put("collectionSupported", true);
            mapUsage(map, "collection", collectionUsage);
            if (m.isCollectionUsageThresholdSupported()) {
                long collectionUsageThresholdCount = m.getCollectionUsageThresholdCount();
                mapThreshold(map, "collection", collectionUsageThresholdCount, m.getCollectionUsageThreshold());
                map.put("collectionThresholdSupported", true);
            } else {
                map.put("collectionThresholdSupported", false);
            }
        } else {
            map.put("collectionSupported", false);
        }
        MemoryUsage usage = m.getUsage();
        if (usage != null) {
            map.put("usageSupported", true);
            mapUsage(map, "usage", usage);
            if (m.isUsageThresholdSupported()) {
                long usageThresholdCount = m.getUsageThresholdCount();
                mapThreshold(map, "usage", usageThresholdCount, m.getUsageThreshold());
                map.put("usageThresholdSupported", true);
            } else {
                map.put("usageThresholdSupported", false);
            }
        } else {
            map.put("usageSupported", false);
        }

        MemoryUsage peak = m.getPeakUsage();
        if (peak != null) {
            map.put("peakSupported", true);
            mapUsage(map, "peak", usage);
        } else {
            map.put("peakSupported", false);
        }
        return map;
    }

    private void mapThreshold(Map<String, Object> map, String string, long collectionUsageThresholdCount,
            long collectionUsageThreshold) {
        map.put(string + "Threshold", Utils.formatBytes(collectionUsageThreshold));
        map.put(string + "ThresholdCount", collectionUsageThresholdCount);
    }

    private void mapUsage(Map<String, Object> map, String string, MemoryUsage usage) {
        map.put(string + "Commited", Utils.formatBytes(usage.getCommitted()));
        map.put(string + "Init", Utils.formatBytes(usage.getInit()));
        long max = usage.getMax();
        long used = usage.getUsed();
        map.put(string + "Used", Utils.formatBytes(used));
        if (max == UNDEFINED_VALUE) {
            map.put(string + "Max", "undefined");
            map.put(string + "Free", "undefined");
        } else {
            map.put(string + "Max", Utils.formatBytes(max));
            map.put(string + "Free", Utils.formatBytes(max - used));
            map.put(string + "Percent", Math.round(100 * (used / max)));
        }
    }

}
