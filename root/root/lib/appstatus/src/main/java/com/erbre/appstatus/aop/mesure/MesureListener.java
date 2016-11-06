package com.erbre.appstatus.aop.mesure;

public interface MesureListener<T> {
    void receive(T event);

}
