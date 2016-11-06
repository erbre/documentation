package com.erbre.documentation.service.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChronoLogger {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChronoLogger.class.getName());

    private Chrono chrono;

    private String msg;

    private ChronoLogger(String msg) {
        this.msg = msg;
    }

    public static ChronoLogger start(String msg) {
        ChronoLogger chronoLogger = new ChronoLogger(msg);
        return chronoLogger.start();
    }

    private ChronoLogger start() {
        this.chrono = new Chrono();
        LOGGER.info(String.format("Starting [%s] at[%s]", msg, chrono.start));
        return this;

    }

    public ChronoLogger success() {
        LOGGER.info(String.format("Report [%s] SUCCESS [OK] ", msg));
        return this;
    }

    public ChronoLogger error(Throwable t) {
        LOGGER.error(String.format("Report [%s] ERROR [KO] ", msg), t);
        return this;
    }

    public ChronoLogger end() {
        chrono.end();
        LOGGER.info(String.format("END [%s] in [%s]ms ", msg, chrono.getTime()));
        return this;
    }

}
