package com.gamusdev.servletreactive.performance.data;

public interface IDataManager {

    void insertPostDuration(final Long duration);
    void insertPutDuration(final Long duration);
    void insertGetDuration(final Long duration);
    void insertGetAllDuration(final Long duration);
    void insertDeleteDuration(final Long duration);

    void printPostInfo();
    void printPutInfo();
    void printGetInfo();
    void printGetAllInfo();
    void printDeleteInfo();

    void printMeanInfo();
}
