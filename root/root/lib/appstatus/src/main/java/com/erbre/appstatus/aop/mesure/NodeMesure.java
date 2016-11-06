package com.erbre.appstatus.aop.mesure;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class NodeMesure implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private double avgTime;
    private String category;

    private Map<String, NodeMesure> children;

    private long count = 1l;
    private long hitRequest = 1l;
    private long endTime = -1l;
    private long errorCount = 0l;
    private String idTarget;
    private long maxTime = Long.MIN_VALUE;
    private long minTime = Long.MAX_VALUE;
    private NodeMesure parent;
    private long startTime = -1l;;
    private long successCount = 0l;;

    private String targetCategory;

    private String thread;

    private long threadId;

    private String threadName;

    public void add(NodeMesure child) {
        if (children == null) {
            children = new HashMap<>();
        }
        child.parent = this;
        children.put(child.getIdTarget(), child);
    }

    public double getAvgTime() {
        return avgTime;
    }

    public String getCategory() {
        return category;
    }

    public Map<String, NodeMesure> getChildren() {
        return children;
    }

    public long getCount() {
        return count;
    }

    public long getEndTime() {
        return endTime;
    }

    public long getErrorCount() {
        return errorCount;
    }

    public String getIdTarget() {
        return idTarget;
    }

    public long getMaxTime() {
        return maxTime;
    }

    public long getMinTime() {
        return minTime;
    }

    public NodeMesure getParent() {
        return parent;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getSuccessCount() {
        return successCount;
    }

    public String getTargetCategory() {
        return targetCategory;
    }

    public String getThread() {
        return thread;
    }

    public long getThreadId() {
        return threadId;
    }

    public String getThreadName() {
        return threadName;
    }

    public boolean hasChildren() {
        return (children == null ? false : !children.isEmpty());
    }

    public boolean hasParent() {
        return (parent == null);
    }

    public void setAvgTime(double avgTime) {
        this.avgTime = avgTime;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setErrorCount(long errorCount) {
        this.errorCount = errorCount;
    }

    public void setIdTarget(String idTarget) {
        this.idTarget = idTarget;
    }

    public void setMaxTime(long maxTime) {
        this.maxTime = maxTime;
    }

    public void setMinTime(long minTime) {
        this.minTime = minTime;
    }

    public void setParent(NodeMesure parent) {
        this.parent = parent;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setSuccessCount(long successCount) {
        this.successCount = successCount;
    }

    public void setTargetCategory(String targetCategory) {
        this.targetCategory = targetCategory;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }

    public void setThreadId(long threadId) {
        this.threadId = threadId;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public NodeMesure getChild(String methodId) {
        return children == null ? null : children.get(methodId);
    }

    public long getHitRequest() {
        return hitRequest;
    }

    public void setHitRequest(long hitRequest) {
        this.hitRequest = hitRequest;
    }

    public String getRootId() {
        if (parent == null) {
            return idTarget;
        } else {
            return parent.getRootId();
        }
    }

}
