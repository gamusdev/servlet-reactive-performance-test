package com.gamusdev.servletreactive.performance.webflux;

import com.gamusdev.servletreactive.performance.webflux.client.IWebfluxClientMeter;
import com.gamusdev.servletreactive.performance.webflux.client.IWebfluxClientMeterFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@Slf4j
public class WebfluxClientTest {

    private static final String HOST = "http://localhost:8090";
    private static final String BASE_URI = "/api/v1/performance/";
    private static final String WEB_FLUX_CLIENT_METER_FACTORY= "webfluxClientMeterFactory";

    private static final int counterLimit = 100;

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(WebfluxClientTest.class, args);

        IWebfluxClientMeterFactory factory = (IWebfluxClientMeterFactory)context.getBean(WEB_FLUX_CLIENT_METER_FACTORY);
        executeWebfluxClient(factory.getInstance(HOST, BASE_URI, counterLimit));

        context.close();
    }

    private static void executeWebfluxClient(IWebfluxClientMeter client) throws InterruptedException {
        log.info("Starting Webflux test...");
        long start = System.nanoTime();

        client.postData();
        while(client.getCounterPost() < counterLimit) {
            Thread.sleep(100);
        }

        client.getAllData();
        while(client.getCounterGetAll() < 1) {
            Thread.sleep(100);
        }

        client.putData();
        while(client.getCounterPut() < counterLimit) {
            Thread.sleep(100);
        }

        client.getData();
        while(client.getCounterGet() < counterLimit) {
            Thread.sleep(100);
        }

        client.deleteData();
        while(client.getCounterDelete() < counterLimit) {
            Thread.sleep(100);
        }

        long end = System.nanoTime();
        log.info("---------------------------------------------------------");
        log.info("Webflux test is finished. Duration=" + (end-start) + " ns");
        log.info("Webflux test is finished. Duration=" + (end-start)/1_000_000_000 + " seg");
        log.info("---------------------------------------------------------");
    }

}
