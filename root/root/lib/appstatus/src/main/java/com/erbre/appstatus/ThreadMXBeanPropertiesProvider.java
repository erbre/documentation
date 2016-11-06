package com.erbre.appstatus;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;
import java.util.Map;

import net.sf.appstatus.core.property.impl.AbstractPropertyProvider;

public class ThreadMXBeanPropertiesProvider extends AbstractPropertyProvider {

    @Override
    public String getCategory() {
        return "ThreadMX";
    }

    @Override
    public Map<String, String> getProperties() {
        ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();
        Map<String, String> info = new HashMap<String, String>();
        info.put("Thread", String.format("Started[%s] Peak[%s] Daemon[%s]", mxBean.getTotalStartedThreadCount(),
                mxBean.getPeakThreadCount(), mxBean.getDaemonThreadCount()));
        long[] ids = mxBean.getAllThreadIds();
        for (long id : ids) {
            ThreadInfo threadInfo = mxBean.getThreadInfo(id);
            info.put(String.format("Thread id[%s]", threadInfo.getThreadId()),
                    String.format("name[%s] state[%s] was blocked [%d] time = [%s]ms, waiting [%d] time = [%s]ms",
                            threadInfo.getThreadName(), threadInfo.getThreadState(), threadInfo.getBlockedCount(),
                            threadInfo.getBlockedTime(), threadInfo.getWaitedCount(), threadInfo.getWaitedTime()));
        }
        info.put("threadCpuTimeSupported", String.format("[%s]", mxBean.isThreadCpuTimeSupported()));

        return info;
    }

}
