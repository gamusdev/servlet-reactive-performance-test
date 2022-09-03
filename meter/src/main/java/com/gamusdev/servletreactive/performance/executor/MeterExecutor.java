package com.gamusdev.servletreactive.performance.executor;

import com.gamusdev.servletreactive.performance.data.DataManager;
import com.gamusdev.servletreactive.performance.webflux.client.IWebFluxClientMeter;
import com.gamusdev.servletreactive.performance.webflux.client.IWebFluxClientMeterFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

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
    private IWebFluxClientMeterFactory factory;

    @Autowired
    private DataManager dataManager;

    @Override
    public void execute() throws InterruptedException {
        IWebFluxClientMeter client = factory.getInstance(host, baseUri, counterLimit);
        log.info("Starting WebFlux test...");
        long start = System.nanoTime();

        //client.postData();
        client.postData(dataManager::insertPostDuration);
        activeWaiting( client::getCounterPost, counterLimit);

        client.getAllData();
        activeWaiting(client::getCounterGetAll, 1);

        client.putData(dataManager::insertPutDuration);
        activeWaiting(client::getCounterPut, counterLimit);

        client.getData(dataManager::insertGetDuration);
        activeWaiting(client::getCounterGet, counterLimit);

        client.deleteData(dataManager::insertDeleteDuration);
        activeWaiting(client::getCounterDelete, counterLimit);

        long end = System.nanoTime();

        dataManager.printPostInfo();
        dataManager.printPutInfo();
        dataManager.printGetInfo();
        dataManager.printDeleteInfo();

        dataManager.printMeanInfo();

        log.info("---------------------------------------------------------");
        log.info("WebFlux test is finished. Duration (nanoseconds)=" + (end-start) + " ns");
        log.info("WebFlux test is finished. Duration=" + (end-start)/1_000_000_000 + " seg");
        log.info("---------------------------------------------------------");
    }

    private void activeWaiting(final Supplier<Integer> counter, final int limit) throws InterruptedException {
        while(counter.get() < limit) {
            Thread.sleep(100);
        }
    }
}
