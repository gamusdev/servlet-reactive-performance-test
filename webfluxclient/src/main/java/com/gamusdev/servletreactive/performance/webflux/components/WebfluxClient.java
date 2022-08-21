package com.gamusdev.servletreactive.performance.webflux.components;

import com.gamusdev.servletreactive.performance.webflux.model.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Component
@Slf4j
public class WebfluxClient {

    private static final String HOST = "http://localhost:8090";
    private static final String BASE_URI = "/api/v1/performance/";

    private static final int counterLimit = 100;
    private static final AtomicInteger counterGetAll = new AtomicInteger();
    private static final AtomicInteger counterGet = new AtomicInteger();
    private static final AtomicInteger counterPost = new AtomicInteger();
    private static final AtomicInteger counterPut = new AtomicInteger();
    private static final AtomicInteger counterDelete = new AtomicInteger();

    public WebfluxClient() throws InterruptedException {
        executeWebfluxClient();
    }

    private void executeWebfluxClient() throws InterruptedException {

        log.info("Starting Webflux test...");
        long start = System.nanoTime();

        WebClient client = WebClient.create( HOST );

        postData(client);
        while(counterPost.get() < counterLimit) {
            Thread.sleep(100);
        }

        getAllData(client);
        while(counterGetAll.get() < 1) {
            Thread.sleep(100);
        }

        putData(client);
        while(counterPut.get() < counterLimit) {
            Thread.sleep(100);
        }

        getData(client);
        while(counterGet.get() < counterLimit) {
            Thread.sleep(100);
        }

        deleteData(client);
        while(counterDelete.get() < counterLimit) {
            Thread.sleep(100);
        }

        long end = System.nanoTime();
        log.info("---------------------------------------------------------");
        log.info("Webflux test is finished. Duration=" + (end-start) + " ns");
        log.info("Webflux test is finished. Duration=" + (end-start)/1_000_000_000 + " seg");
        log.info("---------------------------------------------------------");
    }

    private static void getAllData(WebClient client) {
        if (log instanceof ch.qos.logback.classic.Logger) {
            ((ch.qos.logback.classic.Logger)log).setLevel(ch.qos.logback.classic.Level.INFO);
        }

        log.info("---> GETALL");
        Flux<Data> dataFlux = client.get()
                .uri(BASE_URI)
                .exchangeToFlux(response -> {
                    counterGetAll.incrementAndGet();
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToFlux(Data.class);
                    } else {
                        log.error("Something weird happened. The test is broken. Status Code="+response.statusCode());
                        throw new RuntimeException( "General error" );
                    }
                });
                /*.retrieve()
                .bodyToFlux(Data.class);*/
        //TODO Ver por que el resultado es {"id":null,"data":"oulZlDrieV"}, aunque mete la ID
        dataFlux.subscribe(d -> log.info("---> GETALL: id=" +d.getId()+" data=" + d.getData()));
        log.info("---> END GETALL");
    }

    private static void getData(WebClient client) {
        log.info("---> GET");
        IntStream.rangeClosed(1, counterLimit).forEach(i -> {
            Flux<Data> dataFlux = client.get()
                    .uri(BASE_URI + i)
                    .exchangeToFlux(response -> {
                        counterGet.incrementAndGet();
                        if (response.statusCode().equals(HttpStatus.OK)) {
                            return response.bodyToFlux(Data.class);
                        } else {
                            log.error("Something weird happened. The test is broken. Status Code="+response.statusCode());
                            throw new RuntimeException( "General error" );
                        }
                    });

            dataFlux.subscribe(d -> log.info("---> GET " + i + ": id=" +d.getId()+" data=" + d.getData()));
        });

        log.info("---> END GET");
    }

    private static void postData(WebClient client) {
        log.info("---> POST");

        IntStream.rangeClosed(1, counterLimit).forEach( i -> {
            Mono<Data> result = client.post().uri(BASE_URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(Mono.just(Data.builder().data(
                            RandomStringUtils.randomAlphanumeric(10)
                    ).build()), Data.class)
                    .exchangeToMono(response -> {
                        counterPost.incrementAndGet();
                        if (response.statusCode().equals(HttpStatus.CREATED)) {
                            return response.bodyToMono(Data.class);
                        } else {
                            log.error("Something weird happened. The test is broken. Status Code="+response.statusCode());
                            throw new RuntimeException( "General error" );
                        }
                    });
            result.subscribe( d -> log.info("---> POST id=" +d.getId()+" data=" + d.getData()));
        });
        log.info("---> END POST");
    }

    private static void putData(WebClient client) {
        log.info("---> PUT");
        IntStream.rangeClosed(1, counterLimit).forEach( i -> {
            Mono<Data> result = client.put().uri(BASE_URI+ i)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(Mono.just(Data.builder()
                            .id(i)
                            .data( RandomStringUtils.randomAlphanumeric(10) + "-PUT-"+i )
                            .build()), Data.class)
                    .exchangeToMono(response -> {
                        counterPut.incrementAndGet();
                        if (response.statusCode().equals(HttpStatus.CREATED)) {
                            return response.bodyToMono(Data.class);
                        } else {
                            log.error("Something weird happened. The test is broken. Status Code=" + response.statusCode());
                            throw new RuntimeException("General error");
                        }
                    });
            result.subscribe(d -> log.info("---> PUT id=" + d.getId() + " data=" + d.getData()));
        });
        log.info("---> END PUT");
    }

    private static void deleteData(WebClient client) {
        log.info("---> DELETE");
        IntStream.rangeClosed(1, counterLimit).forEach( i -> {
            Mono<Data> result = client.delete()
                    .uri(BASE_URI + i)
                    //.retrieve();
                    .exchangeToMono(response -> {
                        counterDelete.incrementAndGet();
                        if (response.statusCode().equals(HttpStatus.NO_CONTENT)) {
                            return response.bodyToMono(Data.class);
                        } else {
                            log.error("Something weird happened. The test is broken. Status Code="+response.statusCode());
                            throw new RuntimeException( "General error" );
                        }
                    });
            result.subscribe();
        });
        log.info("---> END DELETE");
    }
}
