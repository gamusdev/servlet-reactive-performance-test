package com.gamusdev.servletreactive.performance.executor;

import com.gamusdev.servletreactive.performance.data.DataManager;
import com.gamusdev.servletreactive.performance.webflux.client.IWebFluxClientMeter;
import com.gamusdev.servletreactive.performance.webflux.client.IWebFluxClientMeterFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(SpringExtension.class)
public class MeterExecutorTest {

    private final static Integer ONE = 1;

    @Mock
    private IWebFluxClientMeterFactory factory;

    @Mock
    private DataManager dataManager;

    @Mock
    private IWebFluxClientMeter client;

    //@Captor
    //ArgumentCaptor<Consumer> consumerCaptor;

    @InjectMocks
    private MeterExecutor meterExecutor;

    @Test
    public void executeTest() throws InterruptedException {
        // When
        ReflectionTestUtils.setField( meterExecutor, "counterLimit", ONE);
        Mockito.when(client.getCounterPost()).thenReturn(ONE);
        Mockito.when(client.getCounterGetAll()).thenReturn(ONE);
        Mockito.when(client.getCounterPut()).thenReturn(ONE);
        Mockito.when(client.getCounterGet()).thenReturn(ONE);
        Mockito.when(client.getCounterDelete()).thenReturn(ONE);

        Mockito.when( factory.getInstance(any(), any(), anyInt(), anyInt()) ).thenReturn(client);

        // Then
        meterExecutor.execute();

        // Verify
        Mockito.verify(client).postData(any());
        Mockito.verify(client).getAllData(any());
        Mockito.verify(client).putData(any());
        Mockito.verify(client).getData(any());
        Mockito.verify(client).deleteData(any());

        //Mockito.verify(client).postData( consumerCaptor.capture() );
        //Consumer consumer = consumerCaptor.getValue();
        //Assertions.assertInstanceOf(Consumer.class, consumer);
    }
}
