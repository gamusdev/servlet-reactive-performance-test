package com.gamusdev.servletreactive.performance.servlet.service;

import com.gamusdev.servletreactive.performance.servlet.model.Data;
import com.gamusdev.servletreactive.performance.servlet.repository.DataRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

/**
 * Data Business Service
 */
@Service
@Log4j2
public class DataService implements IDataService {

    /**
     * Data Repository
     */
    private final DataRepository dataRepository;

    /**
     * Constructor
     */
    @Autowired
    DataService(DataRepository pDataRepository){ this.dataRepository = pDataRepository;}

    /**
     * Return all data records.
     * @return All records
     */
    public List<Data> getAllData() {
        return (List<Data>) this.dataRepository.findAll();
    }

    /**
     * Return data by Id
     */
    public Optional<Data> getDataById(final Integer id) {
        return this.dataRepository.findById(id);
    }

    /**
     * Save a record
     * @param input new data
     * @return saved data with created Id
     */
    public Data postData(final Data input) {
        return this.dataRepository.save( Data.builder().data(input.getData()).build() );
    }

    /**
     * Update a record by Id
     * @param id key
     * @param newData data to update
     * @return the updated page
     */
    public Data updateById(final Integer id, final Data newData ) {
        return dataRepository.save(
                Data.builder()
                        .id(dataRepository.findById(id).get().getId())
                        .data(newData.getData())
                        .build()
        );
    }

    /**
     * Delete by Id
     * @param id key
     * @return Void
     */
    public void delete(@PathVariable final Integer id) {
        dataRepository.deleteById(id);
    }
}
