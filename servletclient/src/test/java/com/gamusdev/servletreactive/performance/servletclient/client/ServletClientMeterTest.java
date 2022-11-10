package com.gamusdev.servletreactive.performance.servletclient.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamusdev.servletreactive.performance.client.common.IClientMeter;
import com.gamusdev.servletreactive.performance.servletclient.model.Data;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.any;

/**
 * Integration test for ServletClientMeter
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
public class ServletClientMeterTest {
    private static final String HOST ="http://localhost";

    private static final String BASE_URI = "/";

    private static final int COUNTER_LIMIT = 1;

    private static final int TIME_BETWEEN_REQUEST = 1;

    private static MockWebServer mockBackEnd;

    private static IClientMeter client;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();

        int port = mockBackEnd.getPort();
        ObjectMapper objectMapper = new ObjectMapper();

        client = ServletClientMeter
                .getInstance(HOST + ":" + port, BASE_URI, COUNTER_LIMIT, TIME_BETWEEN_REQUEST);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    private void prepareTestGetAllOK() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        mockBackEnd.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .setBody(objectMapper.writeValueAsString(Arrays.array(new Data())))
                .addHeader("Content-Type", "application/json")
                .addHeader("duration", "1"));
    }

    private void prepareTestOK() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        mockBackEnd.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .setBody(objectMapper.writeValueAsString(new Data()))
                .addHeader("Content-Type", "application/json")
                .addHeader("duration", "1"));
    }

    private void prepareTestCreated() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        mockBackEnd.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.CREATED.value())
                .setBody(objectMapper.writeValueAsString(new Data()))
                .addHeader("Content-Type", "application/json")
                .addHeader("duration", "1"));
    }

    private void prepareTestNoContent() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        mockBackEnd.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.NO_CONTENT.value())
                .setBody(objectMapper.writeValueAsString(new Data()))
                .addHeader("Content-Type", "application/json")
                .addHeader("duration", "1"));
    }

    private void prepareTestKO() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        mockBackEnd.enqueue(new MockResponse().setResponseCode(HttpStatus.BAD_REQUEST.value())
                .setBody(objectMapper.writeValueAsString(new Data())));
    }

    @Test
    @Order(1)
    public void getAllData() throws InterruptedException, JsonProcessingException {
        // When
        Consumer consumer = Mockito.mock(Consumer.class);

        // Then
        prepareTestGetAllOK();
        client.getAllData(consumer);
//        Thread.sleep(500);

        // Verify
        Assertions.assertEquals(1, client.getCounterGetAll());

        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        Assertions.assertEquals("GET", recordedRequest.getMethod());
        Assertions.assertEquals("/", recordedRequest.getPath());

        Mockito.verify(consumer).accept(any());
    }

    @Test
    @Order(2)
    public void getData() throws InterruptedException, JsonProcessingException {
        // When
        Consumer consumer = Mockito.mock(Consumer.class);

        // Then
        prepareTestOK();
        client.getData(consumer);
//        Thread.sleep(500);

        // Verify
        Assertions.assertEquals(1, client.getCounterGet());

        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        Assertions.assertEquals("GET", recordedRequest.getMethod());
        Assertions.assertEquals("/1", recordedRequest.getPath());

        Mockito.verify(consumer).accept(any());
    }

    @Test
    @Order(3)
    public void postData() throws InterruptedException, JsonProcessingException {
        // When
        Consumer consumer = Mockito.mock(Consumer.class);

        // Then
        prepareTestCreated();
        client.postData(consumer);
//        Thread.sleep(500);

        // Verify
        Assertions.assertEquals(1, client.getCounterPost());

        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        Assertions.assertEquals("POST", recordedRequest.getMethod());
        Assertions.assertEquals("/", recordedRequest.getPath());

        Mockito.verify(consumer).accept(any());
    }

    @Test
    @Order(4)
    public void putData() throws InterruptedException, JsonProcessingException {
        // When
        Consumer consumer = Mockito.mock(Consumer.class);

        // Then
        prepareTestCreated();
        client.putData(consumer);
//        Thread.sleep(500);

        // Verify
        Assertions.assertEquals(1, client.getCounterPut());

        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        Assertions.assertEquals("PUT", recordedRequest.getMethod());
        Assertions.assertEquals("/1", recordedRequest.getPath());

        Mockito.verify(consumer).accept(any());
    }

    @Test
    @Order(5)
    public void deleteData() throws InterruptedException, JsonProcessingException {
        // When
        Consumer consumer = Mockito.mock(Consumer.class);

        // Then
        prepareTestNoContent();
        client.deleteData(consumer);
//        Thread.sleep(500);

        // Verify
        Assertions.assertEquals(1, client.getCounterDelete());

        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        Assertions.assertEquals("DELETE", recordedRequest.getMethod());
        Assertions.assertEquals("/1", recordedRequest.getPath());

        Mockito.verify(consumer).accept(any());
    }

    @Test
    @Order(6)
    public void getAllDataKO() throws InterruptedException, JsonProcessingException {
        // When
        Consumer consumer = Mockito.mock(Consumer.class);

        // Then
        prepareTestKO();

        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            client.getAllData(consumer);
        }, "RuntimeException was expected");

//        Thread.sleep(500);

        //Assertions.assertTrue(thrown.getMessage().contains("General error. Status Code="));

        Assertions.assertEquals(2, client.getCounterGetAll());
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        Assertions.assertEquals("GET", recordedRequest.getMethod());
        Assertions.assertEquals("/", recordedRequest.getPath());

        // Assert that the consumer is not called
        Mockito.verify(consumer, Mockito.times(0)).accept(any());

    }

    @Test
    @Order(7)
    public void getDataKO() throws InterruptedException, JsonProcessingException {
        // When
        Consumer consumer = Mockito.mock(Consumer.class);

        // Then
        prepareTestKO();

        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            client.getData(consumer);
        }, "RuntimeException was expected");

//        Thread.sleep(500);

        Assertions.assertTrue(thrown.getMessage().contains("General error. Status Code="));

        // Verify
        Assertions.assertEquals(2, client.getCounterGet());

        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        Assertions.assertEquals("GET", recordedRequest.getMethod());
        Assertions.assertEquals("/1", recordedRequest.getPath());

        // Assert that the consumer is not called
        Mockito.verify(consumer, Mockito.times(0)).accept(any());
    }

    @Test
    @Order(8)
    public void postDataKO() throws InterruptedException, JsonProcessingException {
        // When
        Consumer consumer = Mockito.mock(Consumer.class);

        // Then
        prepareTestKO();

        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            client.postData(consumer);
        }, "RuntimeException was expected");

//        Thread.sleep(500);

        Assertions.assertTrue(thrown.getMessage().contains("General error. Status Code="));

        // Verify
        Assertions.assertEquals(2, client.getCounterPost());

        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        Assertions.assertEquals("POST", recordedRequest.getMethod());
        Assertions.assertEquals("/", recordedRequest.getPath());

        // Assert that the consumer is not called
        Mockito.verify(consumer, Mockito.times(0)).accept(any());
    }

    @Test
    @Order(9)
    public void putDataKO() throws InterruptedException, JsonProcessingException {
        // When
        Consumer consumer = Mockito.mock(Consumer.class);

        // Then
        prepareTestKO();

        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            client.putData(consumer);
        }, "RuntimeException was expected");

//        Thread.sleep(500);

        Assertions.assertTrue(thrown.getMessage().contains("General error. Status Code="));

        // Verify
        Assertions.assertEquals(2, client.getCounterPut());

        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        Assertions.assertEquals("PUT", recordedRequest.getMethod());
        Assertions.assertEquals("/1", recordedRequest.getPath());

        // Assert that the consumer is not called
        Mockito.verify(consumer, Mockito.times(0)).accept(any());
    }

    @Test
    @Order(10)
    public void deleteDataKO() throws InterruptedException, JsonProcessingException {
        // When
        Consumer consumer = Mockito.mock(Consumer.class);

        // Then
        prepareTestKO();

        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            client.deleteData(consumer);
        }, "RuntimeException was expected");

//        Thread.sleep(500);

        Assertions.assertTrue(thrown.getMessage().contains("General error. Status Code="));

        // Verify
        Assertions.assertEquals(2, client.getCounterDelete());

        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        Assertions.assertEquals("DELETE", recordedRequest.getMethod());
        Assertions.assertEquals("/1", recordedRequest.getPath());

        // Assert that the consumer is not called
        Mockito.verify(consumer, Mockito.times(0)).accept(any());
    }

    /**
     * Test that only one instance is created
     */
    @Test
    @Order(1000)
    public void getInstance() {
        IClientMeter client1 = ServletClientMeter.getInstance(HOST, BASE_URI, COUNTER_LIMIT, TIME_BETWEEN_REQUEST);
        IClientMeter client2 = ServletClientMeter.getInstance(HOST, BASE_URI, COUNTER_LIMIT, TIME_BETWEEN_REQUEST);
        IClientMeter client3 = ServletClientMeter.getInstance("HOST", "BASE_URI", 0, 0);

        Assertions.assertTrue(client1 == client2);
        Assertions.assertTrue(client1 == client3);
    }
}
