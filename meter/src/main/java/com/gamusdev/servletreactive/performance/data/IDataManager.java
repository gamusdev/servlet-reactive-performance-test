package com.gamusdev.servletreactive.performance.data;

public interface IDataManager {

    void insertPostDuration(Long duration);
    void insertPutDuration(Long duration);
    void insertGetDuration(Long duration);
    void insertDeleteDuration(Long duration);

    void printPostInfo();
    void printPutInfo();
    void printGetInfo();
    void printDeleteInfo();


    void printMeanInfo();
}
