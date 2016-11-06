package com.erbre.documentation.service.test;

public abstract class ChronoLoggerTemplate {

    protected abstract void run() throws Exception;

    ChronoLogger logger;

    public void execute(String msg) {
        logger = ChronoLogger.start(msg);
        try {
            run();
            success();
        } catch (Exception ex) {
            error(ex);
        } finally {
            end();
        }
    }

    protected void error(Exception ex) {
        logger.error(ex);
    }

    protected void end() {
        logger.end();
    }

    protected void success() {
        logger.success();
    }

}
