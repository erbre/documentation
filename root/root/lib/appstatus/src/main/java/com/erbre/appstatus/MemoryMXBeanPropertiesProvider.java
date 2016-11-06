package com.erbre.appstatus;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.HashMap;
import java.util.Map;

import net.sf.appstatus.core.property.impl.AbstractPropertyProvider;

public class MemoryMXBeanPropertiesProvider extends AbstractPropertyProvider {

    private static final String EOL = "<br/>";
    private static final long UNDEFINED_VALUE = -1l;

    @Override
    public String getCategory() {
        return "MemoryMXBean";
    }

    @Override
    public Map<String, String> getProperties() {
        Map<String, String> info = new HashMap<String, String>();
        StringBuilder memoryManagerMXBeans = new StringBuilder();
        ManagementFactory.getMemoryManagerMXBeans().forEach(m -> {
            memoryManagerMXBeans.append("Manager[").append(m.getName()).append("] manage pool(s) ");
            for (String p : m.getMemoryPoolNames()) {
                memoryManagerMXBeans.append('[').append(p).append(']');
            }
            memoryManagerMXBeans.append("-");
        });
        memoryManagerMXBeans.deleteCharAt(memoryManagerMXBeans.length() - 1);
        info.put("MemoryManagerMXBeans", memoryManagerMXBeans.toString().replace("-", EOL));

        ManagementFactory.getMemoryPoolMXBeans().forEach(m -> {
            info.put(String.format("MemoryPool[%s]", m.getName()), info(m));
        });
        MemoryMXBean mxBean = ManagementFactory.getMemoryMXBean();
        info.put("Heap", infoUsage(mxBean.getHeapMemoryUsage()));
        info.put("NonHeap", infoUsage(mxBean.getNonHeapMemoryUsage()));
        info.put("ObjectPendingFinalization",
                String.format("[%s] objects", mxBean.getObjectPendingFinalizationCount()));

        return info;
    }

    private String info(MemoryPoolMXBean m) {
        StringBuilder builder = new StringBuilder();
        builder.append("Managed by ");
        for (String manager : m.getMemoryManagerNames()) {
            builder.append('[').append(manager).append("]");
        }
        builder.append(EOL);

        builder.append("Type [").append(m.getType().name()).append(']');
        builder.append(EOL);

        MemoryUsage collectionUsage = m.getCollectionUsage();
        builder.append("Collection ");
        if (collectionUsage != null) {
            builder.append("Usage ");
            builder.append(infoUsage(collectionUsage));
            builder.append("Threshold ");
            if (m.isCollectionUsageThresholdSupported()) {
                builder.append(infoThreshold(m.getCollectionUsageThresholdCount(), m.getCollectionUsageThreshold()));
            } else {
                builder.append("unsupported");
            }
        } else {
            builder.append("unsupported");
        }
        builder.append(EOL);

        MemoryUsage usage = m.getUsage();
        builder.append("Usage ");
        if (usage != null) {
            builder.append("Usage ");
            builder.append(infoUsage(usage));
            builder.append("Threshold ");
            if (m.isUsageThresholdSupported()) {
                builder.append(infoThreshold(m.getUsageThresholdCount(), m.getUsageThreshold()));
            } else {
                builder.append("unsupported");
            }
        } else {
            builder.append("unsupported");
        }
        builder.append(EOL);

        MemoryUsage peak = m.getUsage();
        builder.append("Peak ");
        if (peak != null) {
            builder.append("Usage ");
            builder.append(infoUsage(peak));
        } else {
            builder.append("unsupported");
        }
        builder.append(EOL);

        return builder.toString();
    }

    private String infoUsage(MemoryUsage usage) {
        if (usage == null) {
            return "unkown";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(" commited[").append(Utils.formatBytes(usage.getCommitted())).append(']');
        builder.append(" init[").append(Utils.formatBytes(usage.getInit())).append(']');
        long max = usage.getMax();
        builder.append(" max[").append(Utils.formatBytes(max)).append(']');
        builder.append(" used[").append(Utils.formatBytes(usage.getUsed())).append(']');
        if (max != UNDEFINED_VALUE) {
            builder.append(" free[").append(Utils.formatBytes(max - usage.getUsed())).append(']');
        }
        return builder.toString();
    }

    private String infoThreshold(long thresholdTime, long threshold) {
        return String.format("[%s] reached [%d] time(s)", Utils.formatBytes(threshold), thresholdTime);
    }

}
