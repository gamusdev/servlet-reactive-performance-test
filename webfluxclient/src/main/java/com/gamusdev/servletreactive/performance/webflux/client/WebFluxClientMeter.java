package com.gamusdev.servletreactive.performance.webflux.client;

import com.gamusdev.servletreactive.performance.webflux.model.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.IntStream;
/**
 * Implementation of the Interface IWebFluxClientMeter
 * This implementation defines all the operations that the WebFlux client must have to get the metrics.
 */
@Slf4j
class WebFluxClientMeter implements IWebFluxClientMeter { 

    /**
     * The instance itself.
     * WebFluxClientMeter is a singleton
     */
    private static WebFluxClientMeter instance;

    /**
     * WebClient
     */
    private WebClient client;

    /**
     * The base URI
     */
    private String baseUri;

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
    private WebFluxClientMeter() {

    }

    /**
     * Private constructor for this singleton with parameters.
     * @param host The target host
     * @param baseUri The base URI
     * @param counterLimit The limit o messages received to stop the test
     * @param timeBetweenRequests Time between each request
     */
    private WebFluxClientMeter(final String host, final String baseUri, final int counterLimit,
                               final int timeBetweenRequests) {
        this.client = WebClient.create( host );
        this.baseUri = baseUri;
        this.counterLimit = counterLimit;
        this.timeBetweenRequests = timeBetweenRequests;
    }

    /**
     * Factory method to get the instance
     * @param host The target host
     * @param baseUri The base URI
     * @param counterLimit The limit o messages received to stop the test
     * @param timeBetweenRequests Time between each request
     * @return The WebFluxClientMeter instance
     */
    static IWebFluxClientMeter getInstance(final String host, final String baseUri, final int counterLimit,
                                           final int timeBetweenRequests){
        if (instance == null) {
            instance = new WebFluxClientMeter(host, baseUri, counterLimit, timeBetweenRequests);
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
        //Client Get with the baseUri to retrieve all the data
        Flux<Data> dataFlux = client.get()
                .uri(baseUri)
                .exchangeToFlux(response -> {
                    counterGetAll.incrementAndGet();
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        //If all goes fine!
                        // Consume the metric duration header in the received consumer
                        // TODO Note, this can be done also in the subscribe method. ¿Change it?
                        consumer.accept( Long.parseLong(response.headers().header("duration").get(0)));
                        return response.bodyToFlux(Data.class);
                    } else {
                        //If there is any error, raise exception!
                        log.error("Something weird happened. The test is broken. Status Code="+response.statusCode());
                        throw new RuntimeException( "General error. Status Code="+response.statusCode() );
                    }
                });
                /*.retrieve()
                .bodyToFlux(Data.class);*/

        // Subscribe to the Stream
        // TODO The consumer can be use also here, as subscribe param. ¿Change it?
        dataFlux.subscribe(d -> log.debug("---> GETALL: id=" +d.getId()+" data=" + d.getData()));
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

            Flux<Data> dataFlux = client.get()
                    .uri(baseUri + i)
                    .exchangeToFlux(response -> {
                        counterGet.incrementAndGet();
                        if (response.statusCode().equals(HttpStatus.OK)) {
                            //If all goes fine!
                            // Consume the metric duration header in the received consumer
                            consumer.accept( Long.parseLong(response.headers().header("duration").get(0)));
                            return response.bodyToFlux(Data.class);
                        } else {
                            //If there is any error, raise exception!
                            log.error("Something weird happened. The test is broken. Status Code="+response.statusCode());
                            throw new RuntimeException( "General error" );
                        }
                    });

            // Subscribe to the Stream
            dataFlux.subscribe(d -> log.debug("---> GET " + i + ": id=" +d.getId()+" data=" + d.getData()));
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
        IntStream.rangeClosed(1, counterLimit).forEach( i -> {

            waitTimeBetweenRequests();

            Mono<Data> result = client.post().uri(baseUri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(Mono.just(Data.builder().data(
                            RandomStringUtils.randomAlphanumeric(10)
                    ).build()), Data.class)
                    .exchangeToMono(response -> {
                        counterPost.incrementAndGet();
                        if (response.statusCode().equals(HttpStatus.CREATED)) {
                            //If all goes fine!
                            // Consume the metric duration header in the received consumer
                            consumer.accept( Long.parseLong(response.headers().header("duration").get(0)));
                            return response.bodyToMono(Data.class);
                        } else {
                            //If there is any error, raise exception!
                            log.error("Something weird happened. The test is broken. Status Code="+response.statusCode());
                            throw new RuntimeException( "General error" );
                        }
                    });

            // Subscribe to the Stream
            result.subscribe( d -> log.debug("---> POST id=" +d.getId()+" data=" + d.getData()));
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

            Mono<Data> result = client.put().uri(baseUri+ i)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(Mono.just(Data.builder()
                            .id(i)
                            .data( RandomStringUtils.randomAlphanumeric(10) + "-PUT-"+i )
                            .build()), Data.class)
                    .exchangeToMono(response -> {
                        counterPut.incrementAndGet();
                        if (response.statusCode().equals(HttpStatus.CREATED)) {
                            //If all goes fine!
                            // Consume the metric duration header in the received consumer
                            consumer.accept( Long.parseLong(response.headers().header("duration").get(0)));
                            return response.bodyToMono(Data.class);
                        } else {
                            //If there is any error, raise exception!
                            log.error("Something weird happened. The test is broken. Status Code=" + response.statusCode());
                            throw new RuntimeException("General error");
                        }
                    });

            // Subscribe to the Stream
            result.subscribe(d -> log.debug("---> PUT id=" + d.getId() + " data=" + d.getData()));
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

            Mono<Data> result = client.delete()
                    .uri(baseUri + i)
                    //.retrieve();
                    .exchangeToMono(response -> {
                        counterDelete.incrementAndGet();
                        if (response.statusCode().equals(HttpStatus.NO_CONTENT)) {
                            //If all goes fine!
                            // Consume the metric duration header in the received consumer
                            consumer.accept( Long.parseLong(response.headers().header("duration").get(0)));
                            return response.bodyToMono(Data.class);
                        } else {
                            //If there is any error, raise exception!
                            log.error("Something weird happened. The test is broken. Status Code="+response.statusCode());
                            throw new RuntimeException( "General error" );
                        }
                    });

            // Subscribe to the Stream
            result.subscribe();
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
