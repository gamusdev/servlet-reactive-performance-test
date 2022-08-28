package com.gamusdev.servletreactive.performance.executor;

import com.gamusdev.servletreactive.performance.webflux.client.IWebfluxClientMeter;
import com.gamusdev.servletreactive.performance.webflux.client.IWebfluxClientMeterFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MeterExecutor implements IMeterExecutor{

    @Value("${servletreactive.webflux.host}")
    private String host;

    @Value("${servletreactive.webflux.base_uri}")
    private String baseUri;

    @Value("${servletreactive.webflux.counter_limit}")
    private int counterLimit;

    @Autowired
    private IWebfluxClientMeterFactory factory;

    @Override
    public void execute() throws InterruptedException {
        IWebfluxClientMeter client = factory.getInstance(host, baseUri, counterLimit);
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
