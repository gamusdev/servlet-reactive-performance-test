package com.gamusdev.servletreactive.performance.webflux.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamusdev.servletreactive.performance.webflux.model.Data;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;


/**
 * Integration test for WebfluxClientMeter
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
public class WebfluxClientMeterTest {
    private static final String HOST ="http://localhost";

    private static final String BASE_URI = "/";

    private static final int COUNTER_LIMIT = 1;

    private static MockWebServer mockBackEnd;

    private static IWebfluxClientMeter client;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();

        int port = mockBackEnd.getPort();
        ObjectMapper objectMapper = new ObjectMapper();

        client = WebfluxClientMeter
                .getInstance(HOST + ":" + port, BASE_URI, COUNTER_LIMIT);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    private void prepareTestOK() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        mockBackEnd.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .setBody(objectMapper.writeValueAsString(new Data()))
                .addHeader("Content-Type", "application/json"));
    }

    private void prepareTestCreated() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        mockBackEnd.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.CREATED.value())
                .setBody(objectMapper.writeValueAsString(new Data()))
                .addHeader("Content-Type", "application/json"));
    }

    private void prepareTestNoContent() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        mockBackEnd.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.NO_CONTENT.value())
                .setBody(objectMapper.writeValueAsString(new Data()))
                .addHeader("Content-Type", "application/json"));
    }

    private void prepareTestKO() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        mockBackEnd.enqueue(new MockResponse().setResponseCode(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    @Order(1)
    public void getAllData() throws InterruptedException, JsonProcessingException {
        // Then
        prepareTestOK();
        client.getAllData();
        Thread.sleep(500);

        // Verify
        Assertions.assertEquals(1, client.getCounterGetAll());

        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        Assertions.assertEquals("GET", recordedRequest.getMethod());
        Assertions.assertEquals("/", recordedRequest.getPath());
    }

    @Test
    @Order(2)
    public void getData() throws InterruptedException, JsonProcessingException {
        // Then
        prepareTestOK();
        client.getData();
        Thread.sleep(500);

        // Verify
        Assertions.assertEquals(1, client.getCounterGet());

        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        Assertions.assertEquals("GET", recordedRequest.getMethod());
        Assertions.assertEquals("/1", recordedRequest.getPath());
    }

    @Test
    @Order(3)
    public void postData() throws InterruptedException, JsonProcessingException {
        // Then
        prepareTestCreated();
        client.postData();
        Thread.sleep(500);

        // Verify
        Assertions.assertEquals(1, client.getCounterPost());

        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        Assertions.assertEquals("POST", recordedRequest.getMethod());
        Assertions.assertEquals("/", recordedRequest.getPath());
    }

    @Test
    @Order(4)
    public void postPut() throws InterruptedException, JsonProcessingException {
        // Then
        prepareTestCreated();
        client.putData();
        Thread.sleep(500);

        // Verify
        Assertions.assertEquals(1, client.getCounterPut());

        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        Assertions.assertEquals("PUT", recordedRequest.getMethod());
        Assertions.assertEquals("/1", recordedRequest.getPath());
    }

    @Test
    @Order(5)
    public void postDelete() throws InterruptedException, JsonProcessingException {
        // Then
        prepareTestNoContent();
        client.deleteData();
        Thread.sleep(500);

        // Verify
        Assertions.assertEquals(1, client.getCounterDelete());

        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        Assertions.assertEquals("DELETE", recordedRequest.getMethod());
        Assertions.assertEquals("/1", recordedRequest.getPath());
    }

    @Test
    @Order(6)
    public void getAllDataKO() throws InterruptedException, JsonProcessingException {
        // Then
        prepareTestKO();
        client.getAllData();

        Thread.sleep(500);

        Assertions.assertEquals(2, client.getCounterGetAll());
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        Assertions.assertEquals("GET", recordedRequest.getMethod());
        Assertions.assertEquals("/", recordedRequest.getPath());
    }

    @Test
    @Order(7)
    public void getDataKO() throws InterruptedException, JsonProcessingException {
        // Then
        prepareTestKO();
        client.getData();
        Thread.sleep(500);

        // Verify
        Assertions.assertEquals(2, client.getCounterGet());

        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        Assertions.assertEquals("GET", recordedRequest.getMethod());
        Assertions.assertEquals("/1", recordedRequest.getPath());
    }

    @Test
    @Order(8)
    public void postDataKO() throws InterruptedException, JsonProcessingException {
        // Then
        prepareTestKO();
        client.postData();
        Thread.sleep(500);

        // Verify
        Assertions.assertEquals(2, client.getCounterPost());

        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        Assertions.assertEquals("POST", recordedRequest.getMethod());
        Assertions.assertEquals("/", recordedRequest.getPath());
    }

    @Test
    @Order(9)
    public void postPutKO() throws InterruptedException, JsonProcessingException {
        // Then
        prepareTestKO();
        client.putData();
        Thread.sleep(500);

        // Verify
        Assertions.assertEquals(2, client.getCounterPut());

        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        Assertions.assertEquals("PUT", recordedRequest.getMethod());
        Assertions.assertEquals("/1", recordedRequest.getPath());
    }

    @Test
    @Order(10)
    public void postDeleteKO() throws InterruptedException, JsonProcessingException {
        // Then
        prepareTestKO();
        client.deleteData();
        Thread.sleep(500);

        // Verify
        Assertions.assertEquals(2, client.getCounterDelete());

        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        Assertions.assertEquals("DELETE", recordedRequest.getMethod());
        Assertions.assertEquals("/1", recordedRequest.getPath());
    }

    /**
     * Test that only one instance is created
     */
    @Test
    @Order(1000)
    public void getInstance() {
        IWebfluxClientMeter client1 = WebfluxClientMeter.getInstance(HOST, BASE_URI, COUNTER_LIMIT);
        IWebfluxClientMeter client2 = WebfluxClientMeter.getInstance(HOST, BASE_URI, COUNTER_LIMIT);
        IWebfluxClientMeter client3 = WebfluxClientMeter.getInstance("HOST", "BASE_URI", 0);

        Assertions.assertTrue(client1 == client2);
        Assertions.assertTrue(client1 == client3);
    }
}
