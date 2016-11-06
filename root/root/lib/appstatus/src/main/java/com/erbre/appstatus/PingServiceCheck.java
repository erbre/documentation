package com.erbre.appstatus;

import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.appstatus.core.check.AbstractCheck;
import net.sf.appstatus.core.check.CheckResultBuilder;
import net.sf.appstatus.core.check.ICheck;
import net.sf.appstatus.core.check.ICheckResult;

public class PingServiceCheck extends AbstractCheck implements ICheck {

    private static final Logger LOGGER = LoggerFactory.getLogger(PingServiceCheck.class.getName());

    private String address;
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    private Map<String, String> queryParams;

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(Map<String, String> queryParams) {
        this.queryParams = queryParams;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String getGroup() {
        return "RestService";
    }

    private String name;
    
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return (name==null? address+path : name);
    }

    @Override
    public ICheckResult checkStatus(Locale locale) {

        CheckResultBuilder resultBuilder = new CheckResultBuilder();
        resultBuilder.from(this);
        try {
            Client client = ClientBuilder.newClient();
            WebTarget target = client.target(address);
            target = target.path(path);
            if (queryParams != null) {
                for (Entry<String, String> entry : queryParams.entrySet()) {
                    target.queryParam(entry.getKey(), entry.getValue());
                }
            }
            Invocation.Builder builder = target.request();
            Response response = builder.get();
            Date date = response.getDate();
            int code = response.getStatusInfo().getStatusCode();
            String family = response.getStatusInfo().getFamily().name();
            String reason = response.getStatusInfo().getReasonPhrase();
            resultBuilder.description(String.format("Check Rest Service [%s] [%s] at [%tc] => code[%d] [%s] [%s]",
                    address, path, date, code, family, reason));
            if (code == 200) {
                resultBuilder.code(OK);
            } else {
                resultBuilder.resolutionSteps(String.format("Check server status or address for service [%s] [%s]", address,path));
                resultBuilder.code(FATAL);
                resultBuilder.fatal();
            }
        } catch (Exception ex) {
            LOGGER.error(String.format("Error in call service [%s][%s]", address, path), ex);
            resultBuilder.description(String.format("Unavailable service [%s][%s] : Error [%s]", address, path,ex.getMessage()));
            resultBuilder.code(FATAL);
            resultBuilder.resolutionSteps(String.format("Check server status or address for service [%s] [%s]", address,path));
            resultBuilder.fatal();
        }
        return resultBuilder.build();
    }

}
