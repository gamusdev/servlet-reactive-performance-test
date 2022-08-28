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

@Slf4j
// TODO Add support to a MAP to collect all data results
// TODO Or return a performance meter with all the times, and not only the body
    // TODO Or return an enrich object, with body and headers
class WebfluxClientMeter implements IWebfluxClientMeter {

    private static WebfluxClientMeter instance;

    private WebClient client;
    private String baseUri;
    private int counterLimit;

    private static final AtomicInteger counterGetAll = new AtomicInteger();
    private static final AtomicInteger counterGet = new AtomicInteger();
    private static final AtomicInteger counterPost = new AtomicInteger();
    private static final AtomicInteger counterPut = new AtomicInteger();
    private static final AtomicInteger counterDelete = new AtomicInteger();

    private WebfluxClientMeter() {

    }
    private WebfluxClientMeter(final String host, final String baseUri, final int counterLimit) {
        this.client = WebClient.create( host );
        this.baseUri = baseUri;
        this.counterLimit = counterLimit;
    }

    static IWebfluxClientMeter getInstance(final String host, final String baseUri, final int counterLimit){
        if (instance == null) {
            instance = new WebfluxClientMeter(host, baseUri, counterLimit);
        }
        return instance;
    }

    @Override
    public void getAllData() {
        log.debug("---> GETALL");
        Flux<Data> dataFlux = client.get()
                .uri(baseUri)
                .exchangeToFlux(response -> {
                    counterGetAll.incrementAndGet();
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        //TODO Adaptar DataManager para GetAll
                        //log.info("GETALL duration=" + response.headers().header("duration").get(0));
                        return response.bodyToFlux(Data.class);
                    } else {
                        log.error("Something weird happened. The test is broken. Status Code="+response.statusCode());
                        throw new RuntimeException( "General error" );
                    }
                });
                /*.retrieve()
                .bodyToFlux(Data.class);*/
        //TODO Ver por que el resultado es {"id":null,"data":"oulZlDrieV"}, aunque mete la ID
        dataFlux.subscribe(d -> log.debug("---> GETALL: id=" +d.getId()+" data=" + d.getData()));
        log.debug("---> END GETALL");
    }

    @Override
    public void getData() {
        log.debug("---> GET");
        IntStream.rangeClosed(1, counterLimit).forEach(i -> {
            Flux<Data> dataFlux = client.get()
                    .uri(baseUri + i)
                    .exchangeToFlux(response -> {
                        counterGet.incrementAndGet();
                        if (response.statusCode().equals(HttpStatus.OK)) {
                            //log.info("GET duration=" + response.headers().header("duration").get(0));
                            return response.bodyToFlux(Data.class);
                        } else {
                            log.error("Something weird happened. The test is broken. Status Code="+response.statusCode());
                            throw new RuntimeException( "General error" );
                        }
                    });

            dataFlux.subscribe(d -> log.debug("---> GET " + i + ": id=" +d.getId()+" data=" + d.getData()));
        });

        log.debug("---> END GET");
    }

    @Override
    public void postData() {
        log.debug("---> POST");
        IntStream.rangeClosed(1, counterLimit).forEach( i -> {
            Mono<Data> result = client.post().uri(baseUri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(Mono.just(Data.builder().data(
                            RandomStringUtils.randomAlphanumeric(10)
                    ).build()), Data.class)
                    .exchangeToMono(response -> {
                        counterPost.incrementAndGet();
                        if (response.statusCode().equals(HttpStatus.CREATED)) {
                            //log.info("POST duration=" + response.headers().header("duration").get(0));
                            return response.bodyToMono(Data.class);
                        } else {
                            log.error("Something weird happened. The test is broken. Status Code="+response.statusCode());
                            throw new RuntimeException( "General error" );
                        }
                    });
            result.subscribe( d -> log.debug("---> POST id=" +d.getId()+" data=" + d.getData()));
        });
        log.debug("---> END POST");
    }

    @Override
    public void postDataWithRecords(Consumer<Integer> consumer) {
        log.debug("---> POST");
        IntStream.rangeClosed(1, counterLimit).forEach( i -> {
            Mono<Data> result = client.post().uri(baseUri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(Mono.just(Data.builder().data(
                            RandomStringUtils.randomAlphanumeric(10)
                    ).build()), Data.class)
                    .exchangeToMono(response -> {
                        counterPost.incrementAndGet();
                        if (response.statusCode().equals(HttpStatus.CREATED)) {

                            //String aux1 = response.headers().header("duration").get(0);
                            //Integer aux2 = Integer.getInteger(response.headers().header("duration").get(0));
                            consumer.accept( Integer.parseInt(response.headers().header("duration").get(0)));

                            //log.info("POST duration=" + response.headers().header("duration").get(0));
                            return response.bodyToMono(Data.class);
                        } else {
                            log.error("Something weird happened. The test is broken. Status Code="+response.statusCode());
                            throw new RuntimeException( "General error" );
                        }
                    });
            result.subscribe( d -> log.debug("---> POST id=" +d.getId()+" data=" + d.getData()));
        });
        log.debug("---> END POST");
    }

    @Override
    public void putData() {
        log.debug("---> PUT");
        IntStream.rangeClosed(1, counterLimit).forEach( i -> {
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
                            //log.info("PUT duration=" + response.headers().header("duration").get(0));
                            return response.bodyToMono(Data.class);
                        } else {
                            log.error("Something weird happened. The test is broken. Status Code=" + response.statusCode());
                            throw new RuntimeException("General error");
                        }
                    });
            result.subscribe(d -> log.debug("---> PUT id=" + d.getId() + " data=" + d.getData()));
        });
        log.debug("---> END PUT");
    }

    @Override
    public void deleteData() {
        log.debug("---> DELETE");
        IntStream.rangeClosed(1, counterLimit).forEach( i -> {
            Mono<Data> result = client.delete()
                    .uri(baseUri + i)
                    //.retrieve();
                    .exchangeToMono(response -> {
                        counterDelete.incrementAndGet();
                        if (response.statusCode().equals(HttpStatus.NO_CONTENT)) {
                            //log.info("DELETE duration=" + response.headers().header("duration").get(0));
                            return response.bodyToMono(Data.class);
                        } else {
                            log.error("Something weird happened. The test is broken. Status Code="+response.statusCode());
                            throw new RuntimeException( "General error" );
                        }
                    });
            result.subscribe();
        });
        log.debug("---> END DELETE");
    }
    @Override
    public int getCounterGetAll() {
        return counterGetAll.get();
    }
    @Override
    public int getCounterGet() {
        return counterGet.get();
    }
    @Override
    public int getCounterPost() {
        return counterPost.get();
    }
    @Override
    public int getCounterPut() {
        return counterPut.get();
    }
    @Override
    public int getCounterDelete() {
        return counterDelete.get();
    }

}
