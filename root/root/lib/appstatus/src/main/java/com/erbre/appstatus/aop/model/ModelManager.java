package com.erbre.appstatus.aop.model;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.erbre.appstatus.aop.mesure.MesureListener;
import com.erbre.appstatus.aop.mesure.NodeMesure;

public class ModelManager implements MesureListener<NodeMesure>, ModelReader {

    private Map<String, NodeModel> roots;
    private Map<String, ObjectNodeMesure> objects;

    public ModelManager() {
        roots = new HashMap<>();
        objects = new HashMap<>();
    }

    private final Lock lock = new ReentrantLock();

    @Override
    public void receive(NodeMesure event) {
        try {
            lock.lock();
            NodeMesure newMesure = event;

            NodeModel mesure = roots.get(newMesure.getIdTarget());
            if (mesure == null) {
                NodeModel nodeModel = toNodeModel(newMesure);
                roots.put(newMesure.getIdTarget(), nodeModel);
                nodeAdded(nodeModel);
            } else {
                merge(mesure, newMesure);
            }
        } finally {
            lock.unlock();
        }

    }

    private NodeModel toNodeModel(NodeMesure newMesure) {
        NodeModel node = new NodeModel();
        node.setAvgTime(newMesure.getAvgTime());
        node.setCategory(newMesure.getCategory());
        node.setCount(newMesure.getCount());
        node.setEndTime(newMesure.getEndTime());
        node.setErrorCount(newMesure.getErrorCount());
        node.setHitRequest(newMesure.getHitRequest());
        node.setIdTarget(newMesure.getIdTarget());
        node.setMaxTime(newMesure.getMaxTime());
        node.setMinTime(newMesure.getMinTime());
        node.setStartTime(newMesure.getStartTime());
        node.setSuccessCount(newMesure.getSuccessCount());
        node.setTargetCategory(newMesure.getTargetCategory());
        Map<String, NodeMesure> children = newMesure.getChildren();
        if (children != null) {
            children.forEach((k, v) -> {
                node.add(toNodeModel(v));
            });

        }
        return node;
    }

    private NodeMesure nodeAdded(NodeModel newMesure) {
        // String idTarget = newMesure.getIdTarget();
        // ObjectNodeMesure object = objects.get(idTarget);
        // if (object == null) {
        // object = new ObjectNodeMesure();
        // objects.put(idTarget, object);
        // }
        // object.addMesure(newMesure);
        // return newMesure;
        return null;
    }

    private NodeMesure nodeUpdated(NodeModel newMesure) {
        // String idTarget = newMesure.getIdTarget();
        // ObjectNodeMesure object = objects.get(idTarget);
        // object.updateMesure(newMesure);
        // return newMesure;
        return null;
    }

    public void merge(NodeModel mesure, NodeMesure newMesure) {
        mesure.setStartTime(newMesure.getStartTime());
        mesure.setEndTime(newMesure.getEndTime());
        mesure.setAvgTime(((mesure.getAvgTime() * mesure.getCount()) + (newMesure.getAvgTime() * newMesure.getCount()))
                / (mesure.getCount() + newMesure.getCount()));
        mesure.setCount(newMesure.getCount() + mesure.getCount());
        mesure.setErrorCount(newMesure.getErrorCount() + mesure.getErrorCount());
        mesure.setSuccessCount(newMesure.getSuccessCount() + mesure.getSuccessCount());
        mesure.setMaxTime(Math.max(mesure.getMaxTime(), newMesure.getMaxTime()));
        mesure.setMinTime(Math.min(mesure.getMinTime(), newMesure.getMinTime()));
        mesure.setHitRequest(mesure.getHitRequest() + 1);
        if (newMesure.hasChildren()) {
            newMesure.getChildren().forEach((k, v) -> {
                NodeModel child = mesure.getChild(k);
                if (child == null) {
                    NodeModel newNode = toNodeModel(v);
                    mesure.add(newNode);
                    nodeAdded(newNode);
                } else {
                    merge(child, v);
                }
            });
        }
        nodeUpdated(mesure);

    }

    @Override
    public Map<String, NodeModel> getTrees() {
        return roots;
    }

    @Override
    public Map<String, ObjectNodeMesure> getObjects() {
        return objects;
    }

}
