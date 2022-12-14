package com.gamusdev.servletreactive.performance.meter.data;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class DataManager implements IDataManager{

    private static String TOTAL_NUMBER_REQUEST = "-> Total number of Requests:";
    private static String TOTAL_DURATION = "-> Total duration";
    private static String MEAN_DURATION = "-> Mean duration of the requests";
    private static int NS_TO_MS = 1_000_000;

    private Map<HttpMethod, ArrayDeque<Long>> metrics;
    private long getAllDataDuration;

    public DataManager() {
        this.metrics = new ConcurrentHashMap();
        this.metrics.put(HttpMethod.POST, new ArrayDeque());
        this.metrics.put(HttpMethod.PUT, new ArrayDeque());
        this.metrics.put(HttpMethod.GET, new ArrayDeque());
        this.metrics.put(HttpMethod.DELETE, new ArrayDeque());
    }

    @Override
    public void insertGetAllDuration(final Long duration){
        this.getAllDataDuration = duration;
    }

    @Override
    public void insertPostDuration(final Long duration) {
        metrics.get(HttpMethod.POST).add(duration);
    }

    @Override
    public void insertPutDuration(final Long duration){
        metrics.get(HttpMethod.PUT).add(duration);
    }

    @Override
    public void insertGetDuration(final Long duration){
        metrics.get(HttpMethod.GET).add(duration);
    }

    @Override
    public void insertDeleteDuration(final Long duration){
        metrics.get(HttpMethod.DELETE).add(duration);
    }

    @Override
    public void printPostInfo() {
        printInfo(HttpMethod.POST);
    }

    @Override
    public void printPutInfo() {
        printInfo(HttpMethod.PUT);
    }

    @Override
    public void printGetInfo() {
        printInfo(HttpMethod.GET);
    }

    @Override
    public void printDeleteInfo() {
        printInfo(HttpMethod.DELETE);
    }

    @Override
    public void printGetAllInfo() {
        log.info("----------------- Metrics: Get All data");
        log.info("GET ALL " + TOTAL_DURATION + " (miliseconds):" + this.getAllDataDuration/NS_TO_MS + " ms");
    }

    @Override
    public void printMeanInfo() {
        log.info("----------------- Glocal Metrics:");

        long numRequests = metrics.values().stream().mapToLong(ArrayDeque::size).sum();
        log.info(TOTAL_NUMBER_REQUEST + numRequests);

        double totalDuration = metrics.values().stream().flatMapToLong(v -> v.stream().mapToLong( i -> i)).sum();
        //log.info(TOTAL_DURATION + " (nanoseconds):" + totalDuration + " ns, ");
        log.info(TOTAL_DURATION + " (miliseconds):" + totalDuration/NS_TO_MS + " ms");

        //log.info(MEAN_DURATION + " (nanoseconds):" + totalDuration / numRequests + " ns");
        log.info(MEAN_DURATION + " (miliseconds):" + (totalDuration / numRequests) /NS_TO_MS + " ms");
    }

    private void printInfo(final HttpMethod method) {
        log.info("----------------- Metrics:" + method);
        ArrayDeque methodMetric = metrics.get( method );
        long numRequests = methodMetric.size();
        log.info(method + TOTAL_NUMBER_REQUEST +  numRequests);

        // totalDuration is the sum of the duration of all the request. Remember that the requests executes concurrently,
        // so, this sum will be bigger than the duration of the test.
        double totalDuration = methodMetric.stream().mapToLong(v -> (long) v).sum();
        //log.info(method + TOTAL_DURATION + " (nanoseconds):" + totalDuration + " ns, ");
        log.info(method + TOTAL_DURATION + " (miliseconds):" + totalDuration / NS_TO_MS + " ms");

        //log.info(method + MEAN_DURATION + " (nanoseconds):" + totalDuration / numRequests + " ns");
        log.info(method + MEAN_DURATION + " (miliseconds):" + (totalDuration / numRequests) / NS_TO_MS + " ms");

        //log.info(method + " minimum duration (nanoseconds):" + methodMetric.stream().mapToLong(v -> (long) v).min().getAsLong() + "ns");
        log.info(method + " minimum duration (miliseconds):" +
                methodMetric.stream().mapToLong(v -> (long) v).min().getAsLong() / NS_TO_MS + " ms");

        //log.info(method + " maximum duration (nanoseconds):" + methodMetric.stream().mapToLong(v -> (long) v).max().getAsLong() + "ns");
        log.info(method + " maximum duration (miliseconds):" +
                methodMetric.stream().mapToLong(v -> (long) v).max().getAsLong() / NS_TO_MS + " ms");
    }
}
