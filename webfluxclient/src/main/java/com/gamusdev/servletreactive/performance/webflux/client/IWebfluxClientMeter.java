package com.gamusdev.servletreactive.performance.webflux.client;

import java.util.function.Consumer;

public interface IWebfluxClientMeter {
    void getAllData();
    void getData();
    void postData();
    void putData();
    void deleteData();
    int getCounterGetAll();
    int getCounterGet();
    int getCounterPost();
    int getCounterPut();
    int getCounterDelete();



    void postDataWithRecords(Consumer<Integer> consumer);
}
