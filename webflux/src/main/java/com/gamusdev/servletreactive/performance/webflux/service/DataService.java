package com.gamusdev.servletreactive.performance.webflux.service;

import com.gamusdev.servletreactive.performance.webflux.model.Data;
import com.gamusdev.servletreactive.performance.webflux.repository.DataRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicInteger;

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
    public Flux<Data> getAllData() {
        return this.dataRepository.findAll();
    }

    /**
     * Return data by Id
     */
    public Mono<Data> getDataById(final Integer id) {
        return this.dataRepository.findById(id);
    }

    /**
     * Save a record
     * @param input new data
     * @return saved data with created Id
     */
    public Mono<Data> postData(final Data input) {
        return this.dataRepository.save( Data.builder().data(input.getData()).build() );
    }



    /**
     * Update a record by Id
     * @param id key
     * @param newData data to update
     * @return the updated page
     */
    public Mono<Data> updateById(final Integer id, final Data newData ) {
        return dataRepository.findById(id)
                .flatMap(old -> dataRepository.save(
                        Data.builder()
                                .id(old.getId())
                                .data(newData.getData())
                                .build()
                ));
    }

    /**
     * Delete by Id
     * @param id key
     * @return Void
     */
    public Mono<Void> delete(@PathVariable final Integer id) {
        return dataRepository.deleteById(id);
    }
}
