package com.gamusdev.servletreactive.performance.webflux.service;

import com.gamusdev.servletreactive.performance.webflux.model.Data;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IDataService {
    Flux<Data> getAllData();
    Mono<Data> getDataById(Integer id);
    Mono<Data> postData(final String data);
}
