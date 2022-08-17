package com.gamusdev.servletreactive.performance.webflux.service;

import com.gamusdev.servletreactive.performance.webflux.model.Data;
import com.gamusdev.servletreactive.performance.webflux.repository.DataRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicInteger;

@Service
@Log4j2
public class DataService implements IDataService {

    private final DataRepository dataRepository;

    /**
     * Constructor
     */
    @Autowired
    DataService(DataRepository pDataRepository){ this.dataRepository = pDataRepository;}

    public Flux<Data> getAllData() {
        return this.dataRepository.findAll();
    }

    public Mono<Data> getDataById(final Integer id) {
        return this.dataRepository.findById(id);
    }

    public Mono<Data> postData(final String input) {
        return this.dataRepository.save( Data.builder().data(input).build() );
    }
}
