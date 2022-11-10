package com.gamusdev.servletreactive.performance.meter.executor;

import com.gamusdev.servletreactive.performance.meter.client.IGeneralClientMeterFactory;
import com.gamusdev.servletreactive.performance.meter.data.DataManager;
import com.gamusdev.servletreactive.performance.client.common.IClientMeter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;


/**
 * Implementation for the IMeterExecutor interface
 */
@Service
@Slf4j
public class MeterExecutor implements IMeterExecutor{

    /**
     * Limit of messages received to stop the test
     */
    @Value("${servletreactive.counter_limit}")
    private int counterLimit;

    /**
     * Factory to get the MeterFactory
     */
    @Autowired
    private IGeneralClientMeterFactory factory;

    /**
     * DataManager
     */
    @Autowired
    private DataManager dataManager;

    /**
     * Execute the test
     * @throws InterruptedException InterruptedException
     */
    @Override
    public void execute() throws InterruptedException {
        // Get the client
        IClientMeter client = factory.getInstance();
        log.info("Starting test...");

        // Get the starting time
        long start = System.nanoTime();

        // Post data. For simplicity, active waiting until all the posts are done
        client.postData(dataManager::insertPostDuration);
        activeWaiting( client::getCounterPost, counterLimit);

        // Get all the data with one request. For simplicity, active waiting until done
        client.getAllData(dataManager::insertGetAllDuration);
        activeWaiting(client::getCounterGetAll, 1);

        // Get all the records, one by one. For simplicity, active waiting until done
        client.putData(dataManager::insertPutDuration);
        activeWaiting(client::getCounterPut, counterLimit);

        // Modify all the data. For simplicity, active waiting until done
        client.getData(dataManager::insertGetDuration);
        activeWaiting(client::getCounterGet, counterLimit);

        // Delete the data. For simplicity, active waiting until done
        client.deleteData(dataManager::insertDeleteDuration);
        activeWaiting(client::getCounterDelete, counterLimit);

        // Get the end time
        long end = System.nanoTime();

        // Print the results
        dataManager.printPostInfo();
        dataManager.printPutInfo();
        dataManager.printGetInfo();
        dataManager.printDeleteInfo();

        dataManager.printMeanInfo();

        dataManager.printGetAllInfo();

        log.info("---------------------------------------------------------");
        log.info("WebFlux test is finished. Duration (nanoseconds)=" + (end-start) + " ns");
        log.info("WebFlux test is finished. Duration=" + (end-start)/1_000_000 + " ms");
        log.info("---------------------------------------------------------");
    }

    /**
     * For simplicity, an active waiting is done in each step of the test
     * @param counter Actual number of requests done
     * @param limit Number of request to finish the waiting
     * @throws InterruptedException
     */
    private void activeWaiting(final Supplier<Integer> counter, final int limit) throws InterruptedException {
        while(counter.get() < limit) {
            Thread.sleep(100);
        }
        log.info("Active waiting finished");
    }
}
