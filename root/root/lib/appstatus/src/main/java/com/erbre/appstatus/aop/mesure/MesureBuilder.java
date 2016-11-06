package com.erbre.appstatus.aop.mesure;

public interface MesureBuilder<N, M> {

    TreeContext<M> addStart(N evt, TreeContext<M> context);

    TreeContext<M> addError(N value, TreeContext<M> context);

    TreeContext<M> addSuccess(N value, TreeContext<M> context);

}
