package com.gamusdev.servletreactive.performance.webflux.client;

import com.gamusdev.servletreactive.performance.client.common.IClientMeter;
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

    private static final int TIME_BETWEEN_REQUEST = 1;

    @Mock
    private IClientMeter clientMeter;

    @InjectMocks
    private WebFluxClientMeterFactory factory;

    @Test
    public void getInstance() {

        // Prepare static mocks
        try (MockedStatic<WebFluxClientMeter> utilities = Mockito.mockStatic(WebFluxClientMeter.class)) {
            // When
            utilities.when(() -> WebFluxClientMeter.getInstance(HOST, BASE_URI, COUNTER_LIMIT, TIME_BETWEEN_REQUEST))
                    .thenReturn( clientMeter );
            // Then
            IClientMeter result = factory.getInstance(HOST, BASE_URI, COUNTER_LIMIT, TIME_BETWEEN_REQUEST);

            // Verify
            Assertions.assertThat(clientMeter).isEqualTo(result);
        }
    }
}
