package com.gamusdev.servletreactive.performance.webflux.service;

import com.gamusdev.servletreactive.performance.webflux.model.Data;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IDataService {
    /**
     * Return all data entries.
     * @return All entries
     */
    Flux<Data> getAllData();

    /**
     * Return data record by Id
     */
    Mono<Data> getDataById(Integer id);

    /**
     * Save a data record
     * @param data new data
     * @return saved data with created Id
     */
    Mono<Data> postData(final Data data);

    /**
     * Update a record by Id
     * @param id key
     * @param newPage data to update
     * @return the updated record
     */
    Mono<Data> updateById(Integer id, Data newPage);

    /**
     * Delete by Id
     * @param id key
     * @return Void
     */
    Mono<Void> delete(@PathVariable Integer id);
}
