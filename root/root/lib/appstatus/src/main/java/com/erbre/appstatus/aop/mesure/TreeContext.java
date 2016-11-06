package com.erbre.appstatus.aop.mesure;

import java.io.Serializable;

public class TreeContext<T> implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean ended;

    public boolean isEnded() {
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    private T root;
    private T current;

    public T getRoot() {
        return root;
    }

    public void setRoot(T root) {
        this.root = root;
    }

    public T getCurrent() {
        return current;
    }

    public void setCurrent(T current) {
        if (current == null) {
            ended = true;
        }
        this.current = current;
    }

    public T flush() {
        final T retour = root;
        root = null;
        current = null;
        return retour;
    }

}
