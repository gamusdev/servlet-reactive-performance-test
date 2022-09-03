package com.gamusdev.servletreactive.performance.webflux.client;

import java.util.function.Consumer;

public interface IWebFluxClientMeter {
    void getAllData();
    void getData(Consumer<Long> consumer);
    void postData(Consumer<Long> consumer);
    void putData(Consumer<Long> consumer);
    void deleteData(Consumer<Long> consumer);
    int getCounterGetAll();
    int getCounterGet();
    int getCounterPost();
    int getCounterPut();
    int getCounterDelete();
}
