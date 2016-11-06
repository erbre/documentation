package com.erbre.appstatus.aop.mesure;

import com.erbre.appstatus.aop.notif.FullNotif;

public class TreeNodeMesureBuilder implements MesureBuilder<FullNotif, NodeMesure> {

    @Override
    public TreeContext<NodeMesure> addStart(FullNotif value, TreeContext<NodeMesure> context) {
        if (context == null) {
            context = new TreeContext<>();
            context.setCurrent(newChild(value, null));
            context.setRoot(context.getCurrent());
        } else {
            NodeMesure current = context.getCurrent();
            NodeMesure child = current.getChild(value.getMethodId());
            if (child == null) {
                child = newChild(value, current);
                current.add(child);
            } else {
                child.setCount(child.getCount() + 1);
                child.setStartTime(child.getStartTime());
            }
            context.setCurrent(child);
        }
        return context;
    }

    protected NodeMesure newChild(FullNotif value, NodeMesure current) {
        NodeMesure child = new NodeMesure();
        final String threadName = new StringBuilder().append(value.getThreadName()).append('[')
                .append(value.getThreadId()).append(']').toString();
        child.setThread(threadName);
        child.setCount(1l);
        child.setHitRequest(1l);
        child.setStartTime(value.getStartTime());
        child.setIdTarget(value.getMethodId());
        child.setTargetCategory(value.getMethodName());
        child.setCategory(value.getTarget());
        child.setParent(current);
        return child;
    }

    @Override
    public TreeContext<NodeMesure> addSuccess(FullNotif value, TreeContext<NodeMesure> context) {
        context.getCurrent().setSuccessCount(context.getCurrent().getSuccessCount() + 1);
        mergeEnd(context.getCurrent(), value);
        context.setCurrent(context.getCurrent().getParent());
        return context;
    }

    @Override
    public TreeContext<NodeMesure> addError(FullNotif value, TreeContext<NodeMesure> context) {
        context.getCurrent().setErrorCount(context.getCurrent().getErrorCount() + 1);
        mergeEnd(context.getCurrent(), value);
        context.setCurrent(context.getCurrent().getParent());
        return context;
    }

    private void mergeEnd(NodeMesure mesure, FullNotif value) {
        mesure.setEndTime(value.getEndTime());
        final long time = mesure.getEndTime() - mesure.getStartTime();
        mesure.setMaxTime(Math.max(time, mesure.getMaxTime()));
        mesure.setMinTime(Math.min(time, mesure.getMinTime()));
        final long count = mesure.getCount();
        mesure.setAvgTime(((mesure.getAvgTime() * (count - 1)) + time) / count);
    }

}
