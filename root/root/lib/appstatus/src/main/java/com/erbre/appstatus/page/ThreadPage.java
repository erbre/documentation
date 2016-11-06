package com.erbre.appstatus.page;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.erbre.appstatus.Utils;

public class ThreadPage extends AppstatusPage {

    private static final long UNDEFINED = -1l;

    public String getId() {
        return "ThreadDetails";
    }

    public String getName() {
        return "Thread Details";
    }

    @Override
    protected Map<String, Object> getModel(HttpServletRequest req) {
        ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();
        Map<String, Object> model = new HashMap<>();
        model.put("totalStartedThreadCount", mxBean.getTotalStartedThreadCount());
        model.put("peakThreadCount", mxBean.getPeakThreadCount());
        int daemonThreadCount = mxBean.getDaemonThreadCount();
        model.put("daemonThreadCount", daemonThreadCount);
        int threadCount = mxBean.getThreadCount();
        model.put("threadCount", threadCount);
        model.put("nonDaemonThreadCount", threadCount - daemonThreadCount);
        model.put("threadCpuTimeSupported", mxBean.isThreadCpuTimeSupported());
        model.put("objectMonitorSupported", mxBean.isObjectMonitorUsageSupported());
        model.put("synchronizerUsageSupported", mxBean.isSynchronizerUsageSupported());
        model.put("threadContentionMonitoringEnabled", mxBean.isThreadContentionMonitoringEnabled());
        model.put("threadCpuTimeEnabled", mxBean.isThreadCpuTimeEnabled());
        long totalCpuTime = 0;
        long totalUserTime = 0;
        long totalWaitingTime = 0;
        long totalBlockedTime = 0;
        long totalWaitingCount = 0;
        long totalBlockedCount = 0;
        long suspendedCount = 0;
        long lockCount = 0;

        List<Object> threads = new ArrayList<>();
        long[] ids = mxBean.getAllThreadIds();
        Arrays.sort(ids);
        for (long id : ids) {
            Map<String, Object> threadInfoMap = new HashMap<>();
            ThreadInfo threadInfo = mxBean.getThreadInfo(id, 1);
            threadInfoMap.put("id", id);
            long threadCpuTime = mxBean.getThreadCpuTime(id);
            if (threadCpuTime != UNDEFINED) {
                totalCpuTime += threadCpuTime;
            }
            threadInfoMap.put("cpuTime", Utils.formatNanoSeconds(threadCpuTime));
            long threadUserTime = mxBean.getThreadUserTime(id);
            if (threadUserTime != UNDEFINED) {
                totalUserTime += threadUserTime;
            }
            threadInfoMap.put("userTime", Utils.formatNanoSeconds(threadUserTime));
            threadInfoMap.put("inNative", threadInfo.isInNative());
            boolean suspended = threadInfo.isSuspended();
            threadInfoMap.put("suspended", suspended);
            if (suspended) {
                suspendedCount++;
            }
            String lockName = threadInfo.getLockName();
            threadInfoMap.put("lockName", lockName);
            String lockOwnerName = threadInfo.getLockOwnerName();
            threadInfoMap.put("lockOwnerName", lockOwnerName);
            if (lockName != null || lockOwnerName != null) {
                lockCount++;
            }
            long blockedCount = threadInfo.getBlockedCount();
            totalBlockedCount += blockedCount;
            threadInfoMap.put("blockedCount", blockedCount);
            long blockedTime = threadInfo.getBlockedTime();
            if (blockedTime != UNDEFINED) {
                totalBlockedTime += blockedTime;
            }
            threadInfoMap.put("blockedTime", blockedTime);
            threadInfoMap.put("name", threadInfo.getThreadName());
            threadInfoMap.put("state", threadInfo.getThreadState());
            long waitedCount = threadInfo.getWaitedCount();
            totalWaitingCount += waitedCount;
            threadInfoMap.put("waitedCount", waitedCount);
            long waitedTime = threadInfo.getWaitedTime();
            if (waitedTime != UNDEFINED) {
                totalWaitingTime += waitedTime;
            }
            threadInfoMap.put("waitedTime", waitedTime);
            StackTraceElement[] stackTrace = threadInfo.getStackTrace();
            if (stackTrace != null && stackTrace.length > 0) {
                List<Object> stackTraceList = new ArrayList<>();
                for (StackTraceElement element : stackTrace) {
                    Map<String, Object> stackMap = new HashMap<>();
                    stackMap.put("className", element.getClassName());
                    stackMap.put("fileName", element.getFileName());
                    stackMap.put("methodName", element.getMethodName());
                    int lineNumber = element.getLineNumber();
                    if (lineNumber < 0) {
                        stackMap.put("lineNumber", "?");
                    } else {
                        stackMap.put("lineNumber", lineNumber);
                    }
                    stackMap.put("nativeMethod", element.isNativeMethod());
                    stackTraceList.add(stackMap);
                }
                threadInfoMap.put("stackTrace", stackTraceList);
            }
            threads.add(threadInfoMap);
        }
        model.put("threads", threads);
        model.put("totalCpuTime", Utils.formatNanoSeconds(totalCpuTime));
        model.put("totalUserTime", Utils.formatNanoSeconds(totalUserTime));
        model.put("totalLocked", lockCount);
        model.put("totalWaitingTime", Utils.formatNanoSeconds(totalWaitingTime));
        model.put("totalBlockedTime", Utils.formatNanoSeconds(totalBlockedTime));
        model.put("totalWaitingCount", totalWaitingCount);
        model.put("totalBlockedCount", totalBlockedCount);
        model.put("totalSuspended", suspendedCount);

        String idParam = req.getParameter("id");
        if (idParam != null) {
            Map<String, Object> threadDetails = new HashMap<>();
            long threadId = Long.parseLong(idParam);
            threadDetails.put("id", threadId);
            ThreadInfo threadInfo = mxBean.getThreadInfo(threadId, Integer.MAX_VALUE);
            threadDetails.put("name", threadInfo.getThreadName());
            StackTraceElement[] stackTrace = threadInfo.getStackTrace();
            List<Object> stackTraceList = new ArrayList<>();
            for (StackTraceElement element : stackTrace) {
                Map<String, Object> stackMap = new HashMap<>();
                stackMap.put("className", element.getClassName());
                stackMap.put("fileName", element.getFileName());
                stackMap.put("methodName", element.getMethodName());
                int lineNumber = element.getLineNumber();
                if (lineNumber < 0) {
                    stackMap.put("lineNumber", "?");
                } else {
                    stackMap.put("lineNumber", lineNumber);
                }
                stackMap.put("nativeMethod", element.isNativeMethod());
                stackTraceList.add(stackMap);
            }
            threadDetails.put("stackTrace", stackTraceList);
            model.put("threadDetails", threadDetails);
        }
        return model;
    }

}
