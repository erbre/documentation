package com.erbre.documentation.cxf.interceptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CorrelationIdManager {

    private static Logger LOGGER = LoggerFactory.getLogger(CorrelationIdManager.class.getName());

    private static final ThreadLocal<CorrelationId> threadLocal = new ThreadLocal<CorrelationId>();

    public static enum HEADERS {
        USER, UUID;
    }

    public static final String UNKNOWN_USER = "unknown";

    private CorrelationIdManager() {
    }

    public static Map<String, List<String>> in(Map<String, List<String>> headers) {
        String user = null;
        String uuid = null;
        if (headers == null) {
            headers = new HashMap<String, List<String>>(2);
        } else {
            user = find(headers, HEADERS.USER.name());
            uuid = find(headers, HEADERS.UUID.name());
        }
        if (uuid == null) {
            uuid = UUID.randomUUID().toString();
            headers.put(HEADERS.UUID.name(), newList(uuid));
            LOGGER.info(String.format("new uuid correlationId generated [%s]", uuid));
        }
        if (user == null) {
            user = UNKNOWN_USER;
            headers.put(HEADERS.USER.name(), newList(user));
            LOGGER.warn("no user found");
        }
        setCorrelationId(user, uuid);
        return headers;
    }

    public static Map<String, List<String>> out(Map<String, List<String>> headers) {
        if (headers == null) {
            headers = new HashMap<String, List<String>>(2);
        }
        CorrelationId id = getCorrelationId();
        String user = null;
        String uuid = null;
        if (id == null) {
            LOGGER.warn("no correlation id");
            user = UNKNOWN_USER;
            uuid = UUID.randomUUID().toString();
        } else {
            user = id.getUser();
            uuid = id.getUuid();
        }
        headers.put(HEADERS.UUID.name(), newList(uuid));
        headers.put(HEADERS.USER.name(), newList(user));
        return headers;
    }

    private static List<String> newList(String item) {
        ArrayList<String> list = new ArrayList<>(1);
        list.add(item);
        return list;
    }

    private static String find(Map<String, List<String>> headers, String headerKey) {
        List<String> values = headers.get(headerKey);
        if (values == null || values.isEmpty()) {
            return null;
        } else {
            return values.get(0);
        }
    }

    public static CorrelationId getCorrelationId() {
        return threadLocal.get();
    }

    private static CorrelationId setCorrelationId(String user, String value) {
        CorrelationId id = new CorrelationId();
        id.setUser(user);
        id.setUuid(value);
        threadLocal.set(id);
        return id;
    }

}
