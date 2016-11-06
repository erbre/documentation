package com.erbre.documentation.cxf.interceptor;

public final class CorrelationId {

    private String uuid;

    public String getUuid() {
        return uuid;
    }

    void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUser() {
        return user;
    }

    void setUser(String user) {
        this.user = user;
    }

    private String user;

    CorrelationId() {
    }

    public String toString() {
        return String.format("uuid[%s]:user[%s]", uuid, user);
    }
}
