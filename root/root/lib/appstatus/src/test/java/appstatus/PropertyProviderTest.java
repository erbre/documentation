package appstatus;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.appstatus.core.property.IPropertyProvider;

public abstract class PropertyProviderTest<T extends IPropertyProvider> {

    public T propertyProvider;

    @Before
    public void init() {
        propertyProvider = newInstance();
        
    }

    protected abstract T newInstance(); 

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyProviderTest.class.getName());

    @Test
    public void testPropertiesProvider() {
        if (propertyProvider==null) {
            init();
        }
        Assert.assertNotNull(propertyProvider);
        LOGGER.info(String.format("Testing PropertyProvider[%s] category[%s]", propertyProvider.getClass().getName(),
                propertyProvider.getCategory()));

        propertyProvider.getProperties().forEach((k, v) -> {
            LOGGER.info(String.format("Property [%s]=[%s]", k, v));
        });
    }
}
