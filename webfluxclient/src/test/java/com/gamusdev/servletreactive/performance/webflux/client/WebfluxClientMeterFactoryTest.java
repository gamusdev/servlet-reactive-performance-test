package com.gamusdev.servletreactive.performance.webflux.client;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class WebfluxClientMeterFactoryTest {

    private static final String HOST ="host";

    private static final String BASE_URI = "baseUri";

    private static final int COUNTER_LIMIT = 0;

    @Mock
    private IWebfluxClientMeter clientMeter;

    @InjectMocks
    private WebfluxClientMeterFactory factory;

    @Test
    public void getInstance() {

        // Prepare static mocks
        try (MockedStatic<WebfluxClientMeter> utilities = Mockito.mockStatic(WebfluxClientMeter.class)) {
            // When
            utilities.when(() -> WebfluxClientMeter.getInstance(HOST, BASE_URI, COUNTER_LIMIT))
                    .thenReturn( clientMeter );
            // Then
            IWebfluxClientMeter result = factory.getInstance(HOST, BASE_URI, COUNTER_LIMIT);

            // Verify
            Assertions.assertThat(clientMeter).isEqualTo(result);
        }
    }
}
