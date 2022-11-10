package com.gamusdev.servletreactive.performance.servletclient.client;

import com.gamusdev.servletreactive.performance.client.common.IClientMeter;
import com.gamusdev.servletreactive.performance.servletclient.model.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.IntStream;

/**
 * Implementation of the Interface IWebFluxClientMeter
 * This implementation defines all the operations that the WebFlux client must have to get the metrics.
 */
@Slf4j
public class ServletClientMeter implements IClientMeter {

    /**
     * The instance itself.
     * ServletClientMeter is a singleton
     */
    private static ServletClientMeter instance;

    /**
     * RestTemplate
     */
    private RestTemplate client;

    /**
     * The host
     */
    private String host;

    /**
     * The base URI
     */
    private String baseUri;

    /**
     * The url
     */
    private String url;

    /**
     * Limit of messages received to stop the test
     */
    private int counterLimit;
    /**
     * Time between each request
     */
    private int timeBetweenRequests;

    /**
     * Counters
     */
    private static final AtomicInteger counterGetAll = new AtomicInteger();
    private static final AtomicInteger counterGet = new AtomicInteger();
    private static final AtomicInteger counterPost = new AtomicInteger();
    private static final AtomicInteger counterPut = new AtomicInteger();
    private static final AtomicInteger counterDelete = new AtomicInteger();

    /**
     * Private constructor for this singleton without parameters.
     * Not used.
     */
    private ServletClientMeter() {

    }


    /**
     * Private constructor for this singleton with parameters.
     * @param host The target host
     * @param baseUri The base URI
     * @param counterLimit The limit o messages received to stop the test
     * @param timeBetweenRequests Time between each request
     */
    private ServletClientMeter(final String host, final String baseUri, final int counterLimit,
                               final int timeBetweenRequests) {
        this.client = new RestTemplate();
        this.host = host;
        this.baseUri = baseUri;
        this.counterLimit = counterLimit;
        this.timeBetweenRequests = timeBetweenRequests;
        this.url = host + "/" + baseUri;
    }

    /**
     * Factory method to get the instance
     * @param host The target host
     * @param baseUri The base URI
     * @param counterLimit The limit o messages received to stop the test
     * @param timeBetweenRequests Time between each request
     * @return The ServletClientMeter instance
     */
    public static IClientMeter getInstance(final String host, final String baseUri, final int counterLimit,
                                           final int timeBetweenRequests){
        if (instance == null) {
            instance = new ServletClientMeter(host, baseUri, counterLimit, timeBetweenRequests);
        }
        return instance;
    }

    /**
     * Request to get all data.
     * The metric received from the response (duration header) is passed to the consumer.
     * @param consumer Generic consumer to parse the response
     */
    @Override
    public void getAllData(Consumer<Long> consumer) {

        counterGetAll.incrementAndGet();

        try {
            //Client Get with the host & baseUri to retrieve all the data
            ResponseEntity<Data[]> response = client.getForEntity(url, Data[].class);
            if (response.getStatusCode().equals(HttpStatus.OK)) {
                //If all goes fine!
                // Consume the metric duration header in the received consumer
                consumer.accept(Long.parseLong(response.getHeaders().get("duration").get(0)));
            }
        } catch (HttpStatusCodeException ex) {
            //If there is any error, raise exception!
            log.error("Something weird happened. The test is broken. Status Code=" + ex.getRawStatusCode());
            throw new RuntimeException( "General error. Status Code=" + ex.getRawStatusCode() );
        }
    }

    /**
     * Request to get one record.
     * The metric received from the response (duration header) is passed to the consumer.
     * @param consumer Generic consumer to parse the response
     */
    @Override
    public void getData(Consumer<Long> consumer) {
        //Client Get with the baseUri + id to retrieve only one record
        IntStream.rangeClosed(1, counterLimit).forEach(i -> {

            waitTimeBetweenRequests();

            counterGet.incrementAndGet();

            try{
                //Client Get with the host & baseUri to retrieve all the data
                ResponseEntity<Data> response = client.getForEntity(url + i , Data.class);

                if (response.getStatusCode().equals(HttpStatus.OK)) {
                    //If all goes fine!
                    // Consume the metric duration header in the received consumer
                    consumer.accept( Long.parseLong(response.getHeaders().get("duration").get(0)));
                }

            } catch (HttpStatusCodeException ex) {
                //If there is any error, raise exception!
                log.error("Something weird happened. The test is broken. Status Code=" + ex.getRawStatusCode());
                throw new RuntimeException( "General error. Status Code=" + ex.getRawStatusCode() );
            }
        });
    }

    /**
     * Request to post one new record.
     * The metric received from the response (duration header) is passed to the consumer.
     * @param consumer Generic consumer to parse the response
     */
    @Override
    public void postData(Consumer<Long> consumer) {

        //Client Post with the baseUri to create a new record
        IntStream.rangeClosed(1, counterLimit).forEach(i -> {

            waitTimeBetweenRequests();

            HttpEntity<Data> request = new HttpEntity<>(Data.builder().data(
                    RandomStringUtils.randomAlphanumeric(10)
            ).build());

            counterPost.incrementAndGet();

            try{
                ResponseEntity<Data> response = client.postForEntity(url, request, Data.class);

                if (response.getStatusCode().equals(HttpStatus.CREATED)) {
                    //If all goes fine!
                    // Consume the metric duration header in the received consumer
                    consumer.accept( Long.parseLong(response.getHeaders().get("duration").get(0)));
                }
            } catch (HttpStatusCodeException ex) {
                //If there is any error, raise exception!
                log.error("Something weird happened. The test is broken. Status Code=" + ex.getRawStatusCode());
                throw new RuntimeException( "General error. Status Code=" + ex.getRawStatusCode() );
            }
        });
    }

    /**
     * Request to update one record.
     * The metric received from the response (duration header) is passed to the consumer.
     * @param consumer Generic consumer to parse the response
     */
    @Override
    public void putData(Consumer<Long> consumer) {

        //Client Put with the baseUri + id to update the record
        IntStream.rangeClosed(1, counterLimit).forEach( i -> {

            waitTimeBetweenRequests();

            HttpEntity<Data> request = new HttpEntity<>(Data.builder().data(
                    RandomStringUtils.randomAlphanumeric(10)
            ).build());

            counterPut.incrementAndGet();

            try{
                ResponseEntity<Data> response = client.exchange(url + i, HttpMethod.PUT, request, Data.class);

                if (response.getStatusCode().equals(HttpStatus.CREATED)) {
                    //If all goes fine!
                    // Consume the metric duration header in the received consumer
                    consumer.accept( Long.parseLong(response.getHeaders().get("duration").get(0)));
                }
            } catch (HttpStatusCodeException ex) {
                //If there is any error, raise exception!
                log.error("Something weird happened. The test is broken. Status Code=" + ex.getRawStatusCode());
                throw new RuntimeException( "General error. Status Code=" + ex.getRawStatusCode() );
            }
        });

    }

    /**
     * Request to delete a record.
     * The metric received from the response (duration header) is passed to the consumer.
     * @param consumer Generic consumer to parse the response
     */
    @Override
    public void deleteData(Consumer<Long> consumer) {

        //Client Delete with the baseUri + id to delete the desired record
        IntStream.rangeClosed(1, counterLimit).forEach( i -> {

            waitTimeBetweenRequests();

            counterDelete.incrementAndGet();

            try{
                ResponseEntity<Void> response = client.exchange(url + i, HttpMethod.DELETE, null, Void.class);

                if (response.getStatusCode().equals(HttpStatus.NO_CONTENT)) {
                    //If all goes fine!
                    // Consume the metric duration header in the received consumer
                    consumer.accept( Long.parseLong(response.getHeaders().get("duration").get(0)));
                }
            } catch (HttpStatusCodeException ex) {
                //If there is any error, raise exception!
                log.error("Something weird happened. The test is broken. Status Code=" + ex.getRawStatusCode());
                throw new RuntimeException( "General error. Status Code=" + ex.getRawStatusCode() );
            }
        });
    }

    /**
     * Return the number of getAllData requests
     * @return The counter
     */
    @Override
    public int getCounterGetAll() {
        return counterGetAll.get();
    }

    /**
     * Return the number of getData requests
     * @return The counter
     */
    @Override
    public int getCounterGet() {
        return counterGet.get();
    }

    /**
     * Return the number of postData requests
     * @return The counter
     */
    @Override
    public int getCounterPost() {
        return counterPost.get();
    }

    /**
     * Return the number of putData requests
     * @return The counter
     */
    @Override
    public int getCounterPut() {
        return counterPut.get();
    }

    /**
     * Return the number of deleteData requests
     * @return The counter
     */
    @Override
    public int getCounterDelete() {
        return counterDelete.get();
    }

    /**
     * Sleep the thread timeBetweenRequests ms
     */
    private void waitTimeBetweenRequests() {
        try {
            Thread.sleep(timeBetweenRequests);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
