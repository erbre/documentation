package com.erbre.appstatus.aop;

import javax.inject.Inject;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.erbre.appstatus.aop.notif.NotifBuilder;
import com.erbre.appstatus.aop.notif.NotifListener;

public class NotifInterceptor<T> implements MethodInterceptor {

    @Inject
    private NotifBuilder<T> builder;
    @Inject
    private NotifListener<T> listener;

    public void setBuilder(NotifBuilder<T> builder) {
        this.builder = builder;
    }

    public void setListener(NotifListener<T> listener) {
        this.listener = listener;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        final T evt = builder.buildStart(invocation.getThis(), invocation.getMethod(), invocation.getArguments());
        listener.receiveStart(evt);
        boolean error = true;
        try {
            final Object returnObject = invocation.proceed();
            error = false;
            final T evtSuccess = builder.buildSuccess(invocation.getThis(), invocation.getMethod(),
                    invocation.getArguments(), returnObject);
            listener.receiveSuccess(evtSuccess);
            return returnObject;
        } finally {
            if (error) {
                final T evtEnd = builder.buildError(invocation.getThis(), invocation.getMethod(),
                        invocation.getArguments());
                listener.receiveError(evtEnd);
            }

        }

    }

}
