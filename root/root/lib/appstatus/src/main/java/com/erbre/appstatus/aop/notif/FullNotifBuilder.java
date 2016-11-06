package com.erbre.appstatus.aop.notif;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class FullNotifBuilder implements NotifBuilder<FullNotif> {

    @Override
    public FullNotif buildStart(Object target, Method method, Object[] arguments) {
        FullNotif notif = newFullNotif(target, method);
        notif.setStartTime(System.currentTimeMillis());
        // notif.setArguments(buildParam(arguments));
        return notif;
    }

    protected FullNotif newFullNotif(Object target, Method method) {
        FullNotif notif = new FullNotif();
        String targetName = target.getClass().getName();
        notif.setTarget(targetName);
        notif.setMethodName(method.getName());
        notif.setMethodId(buildMethodId(targetName, method));
        notif.setThreadName(Thread.currentThread().getName());
        notif.setThreadId(Thread.currentThread().getId());
        return notif;
    }

    protected String buildMethodId(String idObject, Method method) {
        StringBuilder builder = new StringBuilder();
        builder.append(idObject);
        builder.append('.');
        builder.append(method.getName());
        Class<?>[] paramTypes = method.getParameterTypes();
        if (paramTypes != null && paramTypes.length > 0) {
            for (Class<?> paramType : paramTypes) {
                builder.append('(');
                builder.append(paramType.getName());
                builder.append(',');
            }
            builder.setCharAt(builder.length() - 1, ')');
        }else {
            builder.append("()");
        }
        return builder.toString();
    }

    protected String buildResult(Object returnObject) {
        if (returnObject != null) {
            StringBuilder builder = new StringBuilder();
            builder.append('[');
            builder.append(returnObject);
            builder.append(']');
            return builder.toString();
        }
        return null;

    }

    protected List<String> buildParam(Object[] arguments) {
        if (arguments != null) {
            List<String> builder = new ArrayList<>();
            for (Object arg : arguments) {
                builder.add(arg == null ? "null" : arg.toString());
            }
            return builder;
        }
        return null;
    }

    @Override
    public FullNotif buildSuccess(Object target, Method method, Object[] arguments, Object returnObject) {
        FullNotif notif = newFullNotif(target, method);
        notif.setEndTime(System.currentTimeMillis());
        notif.setSuccess(true);
        // notif.setResult(buildResult(returnObject));
        // notif.setArguments(buildParam(arguments));
        return notif;

    }

    @Override
    public FullNotif buildError(Object target, Method method, Object[] arguments) {
        FullNotif notif = newFullNotif(target, method);
        notif.setEndTime(System.currentTimeMillis());
        notif.setSuccess(false);
        // notif.setResult(buildResult(returnObject));
        // notif.setArguments(buildParam(arguments));
        return notif;

    }

}
