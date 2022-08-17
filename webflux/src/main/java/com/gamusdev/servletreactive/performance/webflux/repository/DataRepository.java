package com.gamusdev.servletreactive.performance.webflux.repository;

import com.gamusdev.servletreactive.performance.webflux.model.Data;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataRepository extends ReactiveCrudRepository<Data, Integer>  {
}
