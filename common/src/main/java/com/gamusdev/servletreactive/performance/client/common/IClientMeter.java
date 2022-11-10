package com.gamusdev.servletreactive.performance.client.common;


import java.util.function.Consumer;

/**
 * Interface IClientMeter
 * This interface defines all the operations that the client must have to get the metrics.
 * The Api Rest methods contain a Consumer<Long> where the returned metric should be passed.
 */
public interface IClientMeter {


    /**
     * Request to get all data
     * @param consumer Generic consumer to parse the response
     */
    void getAllData(Consumer<Long> consumer);

    /**
     * Request to get one record
     * @param consumer Generic consumer to parse the response
     */
    void getData(Consumer<Long> consumer);

    /**
     * Request to post a new record
     * @param consumer Generic consumer to parse the response
     */
    void postData(Consumer<Long> consumer);

    /**
     * Request to update an existing record
     * @param consumer Generic consumer to parse the response
     */
    void putData(Consumer<Long> consumer);

    /**
     * Request to delete a record
     * @param consumer Generic consumer to parse the response
     */
    void deleteData(Consumer<Long> consumer);

    /**
     * Return the number of getAllData requests
     * @return The counter
     */
    int getCounterGetAll();

    /**
     * Return the number of getData requests
     * @return The counter
     */
    int getCounterGet();

    /**
     * Return the number of postData requests
     * @return The counter
     */
    int getCounterPost();

    /**
     * Return the number of putData requests
     * @return The counter
     */
    int getCounterPut();

    /**
     * Return the number of deleteData requests
     * @return The counter
     */
    int getCounterDelete();
}
