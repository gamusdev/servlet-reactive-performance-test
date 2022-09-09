package com.gamusdev.servletreactive.performance.webflux;

import com.gamusdev.servletreactive.performance.webflux.client.IWebFluxClientMeter;
import com.gamusdev.servletreactive.performance.webflux.client.IWebFluxClientMeterFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * SpringBoot main class to execute the test
 * It is written as example of use, but instead of this class, the package meter should be used.
 * This class is not used in the execution of the performance test. It is just an example of use.
 */
@SpringBootApplication
@Slf4j
public class WebFluxClientPerformance {

    private static final String HOST = "http://localhost:8090";
    private static final String BASE_URI = "/api/v1/performance/";
    private static final String WEB_FLUX_CLIENT_METER_FACTORY= "webfluxClientMeterFactory";

    private static final int COUNTER_LIMIT = 100;
    private static final int TIME_BETWEEN_REQUESTS = 1;

    /**
     * Main class
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(WebFluxClientPerformance.class, args);

        IWebFluxClientMeterFactory factory = (IWebFluxClientMeterFactory)context.getBean(WEB_FLUX_CLIENT_METER_FACTORY);
        executeWebFluxClient(factory.getInstance(HOST, BASE_URI, COUNTER_LIMIT, TIME_BETWEEN_REQUESTS));

        context.close();
    }

    /**
     * Example of execution
     * @param client The IWebFluxClientMeter used
     * @throws InterruptedException Exception
     */
    private static void executeWebFluxClient(IWebFluxClientMeter client) throws InterruptedException {
        log.info("Starting WebFlux test...");
        long start = System.nanoTime();

        client.postData(d -> log.info(d.toString()));
        while(client.getCounterPost() < COUNTER_LIMIT) {
            Thread.sleep(100);
        }

        client.getAllData(d -> log.info(d.toString()));
        while(client.getCounterGetAll() < 1) {
            Thread.sleep(100);
        }

        client.putData(d -> log.info(d.toString()));
        while(client.getCounterPut() < COUNTER_LIMIT) {
            Thread.sleep(100);
        }

        client.getData(d -> log.info(d.toString()));
        while(client.getCounterGet() < COUNTER_LIMIT) {
            Thread.sleep(100);
        }

        client.deleteData(d -> log.info(d.toString()));
        while(client.getCounterDelete() < COUNTER_LIMIT) {
            Thread.sleep(100);
        }

        long end = System.nanoTime();
        log.info("---------------------------------------------------------");
        //log.info("WebFlux test is finished. Duration=" + (end-start) + " ns");
        log.info("WebFlux test is finished. Duration=" + (end-start)/1_000_000 + " ms");
        log.info("---------------------------------------------------------");

    }

}
