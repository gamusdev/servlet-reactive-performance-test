package com.gamusdev.servletreactive.performance.data;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class DataManager implements IDataManager{

    Map<HttpMethod, ArrayDeque> metrics;

    DataManager() {
        metrics = new ConcurrentHashMap();
        metrics.put(HttpMethod.POST, new ArrayDeque());
        metrics.put(HttpMethod.PUT, new ArrayDeque());
        metrics.put(HttpMethod.GET, new ArrayDeque());
        metrics.put(HttpMethod.DELETE, new ArrayDeque());
    }

    @Override
    public void insertDuration(HttpMethod httpMethod, Integer duration) {
        metrics.get(httpMethod).add(duration);
    }

    @Override
    public void insertPostDuration(Integer duration) {
        metrics.get(HttpMethod.POST).add(duration);
    }

    @Override
    public void printPostInfo() {
        int numPosts = metrics.get(HttpMethod.POST).size();
        // TODO para 100 Posts, el contador desborda!
        long totalPostDuration = metrics.get(HttpMethod.POST).stream().mapToInt(v -> (int) v).sum();
        log.info("Number of Post Requests:" +  numPosts);
        log.info("Posts total duration (nanoseconds):" + totalPostDuration + " ns, ");
        log.info("Mean duration (nanoseconds):" + totalPostDuration / numPosts + " ns");
        log.info("Minimum duration (nanoseconds):" + metrics.get(HttpMethod.POST).stream().mapToInt(v -> (int) v).min().getAsInt() + "ns");
        log.info("Maximum duration (nanoseconds):" + metrics.get(HttpMethod.POST).stream().mapToInt(v -> (int) v).max().getAsInt() + "ns");
    }
}
