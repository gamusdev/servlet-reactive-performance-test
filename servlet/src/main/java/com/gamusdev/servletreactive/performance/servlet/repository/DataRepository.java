package com.gamusdev.servletreactive.performance.servlet.repository;

import com.gamusdev.servletreactive.performance.servlet.model.Data;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataRepository extends CrudRepository<Data, Integer> {
}
