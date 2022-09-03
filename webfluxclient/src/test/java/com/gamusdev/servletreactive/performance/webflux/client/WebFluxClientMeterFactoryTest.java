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
public class WebFluxClientMeterFactoryTest {

    private static final String HOST ="host";

    private static final String BASE_URI = "baseUri";

    private static final int COUNTER_LIMIT = 0;

    @Mock
    private IWebFluxClientMeter clientMeter;

    @InjectMocks
    private WebFluxClientMeterFactory factory;

    @Test
    public void getInstance() {

        // Prepare static mocks
        try (MockedStatic<WebFluxClientMeter> utilities = Mockito.mockStatic(WebFluxClientMeter.class)) {
            // When
            utilities.when(() -> WebFluxClientMeter.getInstance(HOST, BASE_URI, COUNTER_LIMIT))
                    .thenReturn( clientMeter );
            // Then
            IWebFluxClientMeter result = factory.getInstance(HOST, BASE_URI, COUNTER_LIMIT);

            // Verify
            Assertions.assertThat(clientMeter).isEqualTo(result);
        }
    }
}
