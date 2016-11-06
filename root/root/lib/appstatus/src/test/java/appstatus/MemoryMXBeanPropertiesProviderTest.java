package appstatus;

import com.erbre.appstatus.MemoryMXBeanPropertiesProvider;

public class MemoryMXBeanPropertiesProviderTest extends PropertyProviderTest<MemoryMXBeanPropertiesProvider> {

    @Override
    protected MemoryMXBeanPropertiesProvider newInstance() {
        return new MemoryMXBeanPropertiesProvider();
    }
}
