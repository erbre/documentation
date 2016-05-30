package come.erbre.documentation.cxf.interceptor;

import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;

public class LogInInterceptor extends AbstractLogInterceptor<Message> {

    public LogInInterceptor() {
        super(Phase.RECEIVE);
    }

}
