package com.erbre.appstatus.aop.mesure;

import javax.inject.Inject;

import com.erbre.appstatus.aop.notif.NotifListener;

public class AbstractThreadLocalMesureManager<M, N> implements NotifListener<N> {

    @Inject
    private MesureListener<M> listener;
    @Inject
    private MesureBuilder<N, M> builder;

    public void setBuilder(MesureBuilder<N, M> builder) {
        this.builder = builder;
    }

    public void setListener(MesureListener<M> listener) {
        this.listener = listener;
    }

    private final ThreadLocal<TreeContext<M>> local = new ThreadLocal<>();

    @Override
    public void receiveStart(N evt) {
        local.set(builder.addStart(evt, getContext()));
    }

    protected TreeContext<M> getContext() {
        TreeContext<M> context = local.get();
        return context;
    }

    @Override
    public void receiveSuccess(N evt) {
        TreeContext<M> context = builder.addSuccess(evt, getContext());
        if (context.isEnded()) {
            send(context.flush());
        } else {
            local.set(context);
        }
    }

    @Override
    public void receiveError(N evt) {
        TreeContext<M> context = builder.addError(evt, getContext());
        if (context.isEnded()) {
            send(context.flush());
        } else {
            local.set(context);
        }
    }

    protected void send(M mesure) {
        local.remove();
        listener.receive(mesure);

    }

}
