package com.erbre.appstatus.aop.notif;

public interface NotifListener<T> {
    public void receiveStart(T evt);

    public void receiveSuccess(T evt);

    public void receiveError(T evt);

}
