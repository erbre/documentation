package com.erbre.appstatus.aop.model;

import java.util.Map;

public interface ModelReader {

    Map<String, NodeModel> getTrees();

    Map<String, ObjectNodeMesure> getObjects();

}
