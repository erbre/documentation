package come.erbre.documentation.cxf.interceptor;

import java.util.Collection;
import java.util.Set;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Attachment;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractLogInterceptor<T extends Message> extends AbstractPhaseInterceptor<T> {

    public AbstractLogInterceptor(String phase) {
        super(phase);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractLogInterceptor.class.getName());

    @Override
    public void handleMessage(T message) throws Fault {
        trace(message);
    }

    private void trace(T message) {
        if (LOGGER.isInfoEnabled()) {
            String id = message.getId();
            LOGGER.info(String.format("Message id[%s]", id));
            message.forEach((k, v) -> LOGGER.info(String.format("key[%s] value[%s]", k, v)));
//            Set<String> keys = message.getContextualPropertyKeys();
//            if (keys == null || keys.isEmpty()) {
//                LOGGER.info("No Message Contextual Property");
//            } else {
//                for (String key : keys) {
//                    LOGGER.info(String.format("Message Contextual Property [%s]=[%s]", key,
//                            message.getContextualProperty(key)));
//                }
//            }
            Set<Class<?>> formats = message.getContentFormats();
            if (formats == null || formats.isEmpty()) {
                LOGGER.info("No Message Content Format");
            } else {
                for (Class<?> format : formats) {
                    LOGGER.info(String.format("Message Content Format [%s]", format.getName()));
                }
            }
            Collection<Attachment> attachements = message.getAttachments();
            if (attachements == null || attachements.isEmpty()) {
                LOGGER.info("No Message Attachement");
            } else {
                int i = 0;
                for (Attachment attachement : attachements) {

                    String contentType = (attachement.getDataHandler() != null)
                            ? attachement.getDataHandler().getContentType() : "unknown";
                    LOGGER.info(String.format("Message Attachement n°[%d] contentType=[%s]", i++, contentType));
                    attachement.getHeaderNames().forEachRemaining(
                            k -> LOGGER.info(String.format("header [%s]=[%s]", k, attachement.getHeader(k))));
                }
            }
        }
    }

    @Override
    public void handleFault(T message) {
        LOGGER.error("Message FAULT");
        trace(message);
    }

}
