package com.erbre.appstatus.aop.notif;

import java.io.Serializable;
import java.util.List;

public class FullNotif implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    private List<String> arguments;
    private long endTime;
    private String methodId;
    private String methodName;
    private String result;
    private long startTime;
    private boolean success;
    private String target;
    private long threadId;
    private String threadName;

    public List<String> getArguments() {
        return arguments;
    }

    public long getEndTime() {
        return endTime;
    }

    public String getMethodId() {
        return methodId;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getResult() {
        return result;
    }

    public long getStartTime() {
        return startTime;
    }

    public String getTarget() {
        return target;
    }

    public long getThreadId() {
        return threadId;
    }

    public String getThreadName() {
        return threadName;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setMethodId(String methodSignature) {
        this.methodId = methodSignature;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setThreadId(long threadId) {
        this.threadId = threadId;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

}
