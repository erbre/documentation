package come.erbre.documentation.cxf.interceptor;

import java.util.List;
import java.util.Map;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

public class CorrelationInInterceptor extends AbstractPhaseInterceptor<Message> {

    public CorrelationInInterceptor() {
        super(Phase.RECEIVE);
    }

    @Override
    public void handleMessage(Message message) throws Fault {

        @SuppressWarnings("unchecked")
        Map<String, List<String>> headers = (Map<String, List<String>>) message.get(Message.PROTOCOL_HEADERS);
        headers = CorrelationIdManager.in(headers);
        message.put(Message.PROTOCOL_HEADERS, headers);

    }
}
