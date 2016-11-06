package come.erbre.documentation.cxf.interceptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.erbre.documentation.cxf.interceptor.CorrelationId;
import com.erbre.documentation.cxf.interceptor.CorrelationIdManager;
import com.erbre.documentation.cxf.interceptor.CorrelationIdManager.HEADERS;

public final class CorrelationIdManagerTest {

    
    @Test
    public void inEmpty() {
        Map<String, List<String>> headers = null;
        Map<String, List<String>> headers2 = CorrelationIdManager.in(headers);
        Assert.assertNotNull(headers2);
        Assert.assertNotNull(headers2.get(HEADERS.USER.name()));
        Assert.assertNotNull(headers2.get(HEADERS.UUID.name()));

        CorrelationId id = CorrelationIdManager.getCorrelationId();
        Assert.assertEquals(id.getUser(), headers2.get(HEADERS.USER.name()).get(0));
        Assert.assertEquals(id.getUuid(), headers2.get(HEADERS.UUID.name()).get(0));
        Assert.assertEquals(id.getUser(), CorrelationIdManager.UNKNOWN_USER);
    }

    @Test
    public void inNotEmpty() {
        String userName = "toto";
        String uuid = "uuid";
        ArrayList<String> listUserName = new ArrayList<>();
        listUserName.add(userName);
        ArrayList<String> listUuid = new ArrayList<>();
        listUuid.add(uuid);

        Map<String, List<String>> headers = new HashMap<>();
        headers.put(HEADERS.USER.name(), listUserName);
        headers.put(HEADERS.UUID.name(), listUuid);

        Map<String, List<String>> headers2 = CorrelationIdManager.in(headers);
        Assert.assertNotNull(headers2);
        Assert.assertNotNull(headers2.get(HEADERS.USER.name()));
        Assert.assertNotNull(headers2.get(HEADERS.UUID.name()));

        CorrelationId id = CorrelationIdManager.getCorrelationId();
        Assert.assertEquals(userName, headers2.get(HEADERS.USER.name()).get(0));
        Assert.assertEquals(uuid, headers2.get(HEADERS.UUID.name()).get(0));
        Assert.assertEquals(userName, id.getUser());
        Assert.assertEquals(uuid, id.getUuid());
    }

    @Test
    public void out() {

        String userName = "toto";
        String uuid = "uuid";
        ArrayList<String> listUserName = new ArrayList<>();
        listUserName.add(userName);
        ArrayList<String> listUuid = new ArrayList<>();
        listUuid.add(uuid);

        Map<String, List<String>> headers = new HashMap<>();
        headers.put(HEADERS.USER.name(), listUserName);
        headers.put(HEADERS.UUID.name(), listUuid);

        Map<String, List<String>> headers2 = CorrelationIdManager.in(headers);
        Map<String, List<String>> headers3 = CorrelationIdManager.out(headers2);

        CorrelationId id = CorrelationIdManager.getCorrelationId();
        Assert.assertEquals(userName, headers3.get(HEADERS.USER.name()).get(0));
        Assert.assertEquals(uuid, headers3.get(HEADERS.UUID.name()).get(0));
        Assert.assertEquals(userName, id.getUser());
        Assert.assertEquals(uuid, id.getUuid());

    }

}
