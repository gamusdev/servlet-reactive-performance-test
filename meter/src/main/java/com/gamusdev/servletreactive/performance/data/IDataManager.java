package com.gamusdev.servletreactive.performance.data;

import org.springframework.http.HttpMethod;

public interface IDataManager {

    void insertDuration(HttpMethod httpMethod, Integer duration);
    void insertPostDuration(Integer duration);
    void printPostInfo();
}
