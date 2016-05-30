package com.erbre.documentation.utils.dozer;

import javax.inject.Named;

import org.dozer.DozerEventListener;
import org.dozer.event.DozerEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public class MyDozerEventListener implements DozerEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyDozerEventListener.class);

    @Override
    public void mappingStarted(DozerEvent event) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("Dozer Mapping Started src[%s] dest[%s] value[%s]", event.getSourceObject(),
                    event.getDestinationObject(), event.getDestinationValue()));
        }
    }

    @Override
    public void preWritingDestinationValue(DozerEvent event) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("Dozer Mapping pre writing src[%s] dest[%s] value[%s]", event.getSourceObject(),
                    event.getDestinationObject(), event.getDestinationValue()));
        }

    }

    @Override
    public void postWritingDestinationValue(DozerEvent event) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(String.format("Dozer Mapping post writing src[%s] dest[%s] value[%s]", event.getSourceObject(),
                    event.getDestinationObject(), event.getDestinationValue()));
        }

    }

    @Override
    public void mappingFinished(DozerEvent event) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(String.format("Dozer Mapping Finished src[%s] dest[%s] value[%s]", event.getSourceObject(),
                    event.getDestinationObject(), event.getDestinationValue()));
        }

    }

}
