package com.gamusdev.servletreactive.performance.servlet.service;

import com.gamusdev.servletreactive.performance.servlet.model.Data;

import java.util.List;
import java.util.Optional;

public interface IDataService {
    /**
     * Return all data entries.
     * @return All entries
     */
    List<Data> getAllData();

    /**
     * Return data record by Id
     */
    Optional<Data> getDataById(Integer id);

    /**
     * Save a data record
     * @param data new data
     * @return saved data with created Id
     */
    Data postData(final Data data);

    /**
     * Update a record by Id
     * @param id key
     * @param newPage data to update
     * @return the updated record
     */
    Data updateById(Integer id, Data newPage);

    /**
     * Delete by Id
     * @param id key
     * @return Void
     */
    void delete(Integer id);
}
