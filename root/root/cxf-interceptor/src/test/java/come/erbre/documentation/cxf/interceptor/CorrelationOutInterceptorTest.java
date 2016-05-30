package come.erbre.documentation.cxf.interceptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageImpl;
import org.apache.cxf.phase.Phase;
import org.junit.Assert;
import org.junit.Test;

import come.erbre.documentation.cxf.interceptor.CorrelationIdManager.HEADERS;

public class CorrelationOutInterceptorTest {

    @Test
    public void testNonEmpty() {
        CorrelationOutInterceptor in = new CorrelationOutInterceptor();
        Assert.assertEquals(in.getPhase(), Phase.SETUP);

        String userName = "toto";
        String uuid = "uuid";
        ArrayList<String> listUserName = new ArrayList<>();
        listUserName.add(userName);
        ArrayList<String> listUuid = new ArrayList<>();
        listUuid.add(uuid);

        Map<String, List<String>> headers = new HashMap<>();
        headers.put(HEADERS.USER.name(), listUserName);
        headers.put(HEADERS.UUID.name(), listUuid);

        Message msg = new MessageImpl();
        msg.put(Message.PROTOCOL_HEADERS, headers);
        in.handleMessage(msg);

        @SuppressWarnings("unchecked")
        Map<String, List<String>> headers2 = (Map<String, List<String>>) msg.get(Message.PROTOCOL_HEADERS);

        Assert.assertEquals(headers2.get(HEADERS.USER.name()), headers.get(HEADERS.USER.name()));
        Assert.assertEquals(headers2.get(HEADERS.UUID.name()), headers.get(HEADERS.UUID.name()));
        CorrelationId id = CorrelationIdManager.getCorrelationId();
        Assert.assertEquals(id.getUser(), headers.get(HEADERS.USER.name()).get(0));
        Assert.assertEquals(id.getUuid(), headers.get(HEADERS.UUID.name()).get(0));

    }
}
