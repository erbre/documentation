package com.erbre.appstatus.aop.notif;

import java.lang.reflect.Method;

public interface NotifBuilder<T> {

    T buildStart(Object target, Method method, Object[] arguments);

    T buildError(Object target, Method method, Object[] arguments);

    T buildSuccess(Object target, Method method, Object[] arguments, Object returnObject);

}
