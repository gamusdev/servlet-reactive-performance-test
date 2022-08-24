package com.gamusdev.servletreactive.performance.webflux.client;

public interface IWebfluxClient {
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
}
