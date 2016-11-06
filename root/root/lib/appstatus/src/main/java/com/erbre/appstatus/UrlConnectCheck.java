package com.erbre.appstatus;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.appstatus.core.check.AbstractCheck;
import net.sf.appstatus.core.check.CheckResultBuilder;
import net.sf.appstatus.core.check.ICheck;
import net.sf.appstatus.core.check.ICheckResult;

public class UrlConnectCheck extends AbstractCheck implements ICheck {

    private static final Logger LOGGER = LoggerFactory.getLogger(UrlConnectCheck.class.getName());

    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String getGroup() {
        return "URLConnect";
    }

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return (name == null ? address : name);
    }

    @Override
    public ICheckResult checkStatus(Locale locale) {

        CheckResultBuilder resultBuilder = new CheckResultBuilder();
        resultBuilder.from(this);
        InputStream is = null;
        try {

            URL url = new URL(address);
            is = url.openStream();
            if (is == null) {
                resultBuilder.code(WARN);
                resultBuilder.description(String.format("Failed to get stream to [%s]", address));
            } else {
                resultBuilder.code(OK);
                resultBuilder.description(String.format("Get stream to [%s]", address));
            }
        } catch (Exception ex) {
            LOGGER.error(String.format("Error in call service [%s]", address), ex);
            resultBuilder
                    .description(String.format("Failed to get stream to [%s]: Error [%s]", address, ex.getMessage()));
            resultBuilder.code(FATAL);
            resultBuilder.resolutionSteps(String.format("Check server status or address [%s]", address));
            resultBuilder.fatal();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOGGER.warn(String.format("Error closing connection to service [%s]", address), e);

                }
            }
        }
        return resultBuilder.build();
    }

}
