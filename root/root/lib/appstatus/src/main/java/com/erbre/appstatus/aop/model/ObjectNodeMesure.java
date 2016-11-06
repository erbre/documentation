package com.erbre.appstatus.aop.model;

import java.util.HashMap;
import java.util.Map;

import com.erbre.appstatus.aop.mesure.NodeMesure;

public class ObjectNodeMesure {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private double avgTime;

    private String category;
    private long count = 1l;

    private long endTime = -1l;

    private long errorCount = 0l;

    private String idTarget;

    private long maxTime = Long.MIN_VALUE;

    private long minTime = Long.MAX_VALUE;

    private Map<String, NodeMesure> nodes;

    private long startTime = -1l;

    private long successCount = 0l;

    private String targetCategory;

    public ObjectNodeMesure() {
    }

    public void addMesure(NodeMesure node) {
        if (nodes == null) {
            nodes = new HashMap<>();
            setIdTarget(node.getIdTarget());
            setCategory(node.getCategory());
            setTargetCategory(node.getTargetCategory());
            String parent = node.getRootId();
            nodes.put(parent, node);
        } else {
            double avgSum = getAvgTime() * getCount() + node.getAvgTime() * node.getCount();
            setCount(getCount() + node.getCount());
            setAvgTime(avgSum / getCount());
            setEndTime(node.getEndTime());
            setErrorCount(getErrorCount() + node.getErrorCount());
            setMaxTime(Math.max(getMaxTime(), node.getMaxTime()));
            setMinTime(Math.min(getMinTime(), node.getMinTime()));
            setSuccessCount(getSuccessCount() + node.getSuccessCount());
            String parent = node.getRootId();
            NodeMesure mesure = nodes.get(parent);
            
            nodes.put(parent, node);

        }
    }

    public double getAvgTime() {
        return avgTime;
    }

    public String getCategory() {
        return category;
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

    public Map<String, NodeMesure> getNodes() {
        return nodes;
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
    };

    public void setIdTarget(String idTarget) {
        this.idTarget = idTarget;
    };

    public void setMaxTime(long maxTime) {
        this.maxTime = maxTime;
    }

    public void setMinTime(long minTime) {
        this.minTime = minTime;
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

    private void update() {

//        long max = Long.MIN_VALUE;
//        long min = Long.MAX_VALUE;
//        long endTime = Long.MIN_VALUE;
//        long startTime = Long.MAX_VALUE;
//
//        long countSum = 0l;
//        long errorSum = 0l;
//        long successSum = 0l;
//        double timeSum = 0d;
//        for (NodeMesure node : nodes) {
//            timeSum += node.getAvgTime() * node.getCount();
//            errorSum += node.getErrorCount();
//            successSum += node.getSuccessCount();
//            countSum += node.getCount();
//            max = Math.max(max, node.getMaxTime());
//            min = Math.min(min, node.getMinTime());
//            endTime = Math.max(endTime, node.getEndTime());
//            startTime = Math.min(startTime, node.getStartTime());
//        }
//        setAvgTime(timeSum / countSum);
//        setCount(countSum);
//        setEndTime(endTime);
//        setStartTime(startTime);
//        setErrorCount(errorSum);
//        setMinTime(min);
//        setMaxTime(max);
//        setSuccessCount(successSum);
    }

    public void updateMesure(NodeMesure newMesure) {
//        NodeMesure oldMesure=nodes.get(idRoot);
//        
//        nodes.put(idRoot, newMesure);
    }

}
