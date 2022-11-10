package com.gamusdev.servletreactive.performance.meter.client;

import com.gamusdev.servletreactive.performance.client.common.ClientType;
import com.gamusdev.servletreactive.performance.client.common.IClientMeter;
import com.gamusdev.servletreactive.performance.client.common.IClientMeterFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@PrepareForTest(GeneralClientMeterFactory.class)
public class GeneralClientMeterFactoryTest {

    private final ClientType clientType = ClientType.SERVLET;

    private final String HOST = "host";
    private final String BASE_URI = "baseUri";
    private final int COUNTER_LIMIT = 1;
    private final int TIME_BETWEEN_REQUESTS = 10;

    private GeneralClientMeterFactory generalClientMeterFactory;

    @Test
    public void getInstanceTest() {
        // When
        IClientMeterFactory clientMeterFactory = mock(IClientMeterFactory.class);
        when(clientMeterFactory.getClientType()).thenReturn(clientType);

        IClientMeter clientMeter = mock(IClientMeter.class);

        when(clientMeterFactory.getInstance(HOST, BASE_URI, COUNTER_LIMIT, TIME_BETWEEN_REQUESTS)).
                thenReturn(clientMeter);

        List<IClientMeterFactory> factories = new ArrayList<>();
        factories.add(clientMeterFactory);

        // Create the instance
        generalClientMeterFactory = new GeneralClientMeterFactory(factories);

        ReflectionTestUtils.setField(generalClientMeterFactory, "clientType", clientType.name());
        ReflectionTestUtils.setField(generalClientMeterFactory, "host", HOST);
        ReflectionTestUtils.setField(generalClientMeterFactory, "baseUri", BASE_URI);
        ReflectionTestUtils.setField(generalClientMeterFactory, "counterLimit", COUNTER_LIMIT);
        ReflectionTestUtils.setField(generalClientMeterFactory, "timeBetweenRequests", TIME_BETWEEN_REQUESTS);

        // Then
        IClientMeter result = generalClientMeterFactory.getInstance();

        // Verify
        Assertions.assertEquals(clientMeter, result);
    }
}
