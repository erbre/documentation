package com.erbre.documentation.cxf.interceptor;

import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;

public class LogOutInterceptor extends AbstractLogInterceptor<Message> {

    public LogOutInterceptor() {
        super(Phase.SEND);
    }

    @Override
    protected String getInterceptorName() {
        return "OUT";
    }
}
