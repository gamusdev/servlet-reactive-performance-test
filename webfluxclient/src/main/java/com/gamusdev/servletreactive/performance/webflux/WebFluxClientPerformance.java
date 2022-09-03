package com.gamusdev.servletreactive.performance.webflux;

import com.gamusdev.servletreactive.performance.webflux.client.IWebFluxClientMeter;
import com.gamusdev.servletreactive.performance.webflux.client.IWebFluxClientMeterFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@Slf4j
public class WebFluxClientPerformance {

    private static final String HOST = "http://localhost:8090";
    private static final String BASE_URI = "/api/v1/performance/";
    private static final String WEB_FLUX_CLIENT_METER_FACTORY= "webfluxClientMeterFactory";

    private static final int counterLimit = 100;

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(WebFluxClientPerformance.class, args);

        IWebFluxClientMeterFactory factory = (IWebFluxClientMeterFactory)context.getBean(WEB_FLUX_CLIENT_METER_FACTORY);
        executeWebFluxClient(factory.getInstance(HOST, BASE_URI, counterLimit));

        context.close();
    }

    private static void executeWebFluxClient(IWebFluxClientMeter client) throws InterruptedException {
        log.info("Starting WebFlux test...");
        long start = System.nanoTime();

        client.postData(d -> log.info(d.toString()));
        while(client.getCounterPost() < counterLimit) {
            Thread.sleep(100);
        }

        client.getAllData();
        while(client.getCounterGetAll() < 1) {
            Thread.sleep(100);
        }

        client.putData(d -> log.info(d.toString()));
        while(client.getCounterPut() < counterLimit) {
            Thread.sleep(100);
        }

        client.getData(d -> log.info(d.toString()));
        while(client.getCounterGet() < counterLimit) {
            Thread.sleep(100);
        }

        client.deleteData(d -> log.info(d.toString()));
        while(client.getCounterDelete() < counterLimit) {
            Thread.sleep(100);
        }

        long end = System.nanoTime();
        log.info("---------------------------------------------------------");
        //log.info("WebFlux test is finished. Duration=" + (end-start) + " ns");
        log.info("WebFlux test is finished. Duration=" + (end-start)/1_000_000 + " ms");
        log.info("---------------------------------------------------------");
    }

}
