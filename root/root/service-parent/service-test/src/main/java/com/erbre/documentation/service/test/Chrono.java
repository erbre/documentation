package com.erbre.documentation.service.test;

public class Chrono {
    long start;
    long end;

    public Chrono() {
        start = System.currentTimeMillis();
    }

    public void end() {
        end = System.currentTimeMillis();
    }

    public long getTime() {
        return end - start;
    }
}