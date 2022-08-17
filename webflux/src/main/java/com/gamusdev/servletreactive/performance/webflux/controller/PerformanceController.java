package com.gamusdev.servletreactive.performance.webflux.controller;
import com.gamusdev.servletreactive.performance.webflux.model.Data;
import com.gamusdev.servletreactive.performance.webflux.service.DataService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/performance")
@Log4j2
public class PerformanceController {

    /**
     * PerformanceService
     */
    private final DataService dataService;

    /**
     * Constructor
     * @param
     */
    @Autowired
    PerformanceController(DataService pDataService) { this.dataService = pDataService; }

    @GetMapping()
    public Mono<ResponseEntity<Flux<Data>>> getAllData() {
        final long startNs = System.nanoTime();
        Flux<Data> allData = this.dataService.getAllData();
        return Mono.just(
                ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("duration", (System.nanoTime() - startNs) + "")
                        .body(allData)
        );
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Mono<Data>>> getDataById(@PathVariable final Integer id) {
        final long startNs = System.nanoTime();
        return this.dataService.getDataById(id)
                .map ( p -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("duration", (System.nanoTime() - startNs) + "")
                        .body( Mono.just(p)) )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Data>> postData(@RequestBody final String data) {
        final long startNs = System.nanoTime();
        return this.dataService.postData(data)
                .map(p-> ResponseEntity
                        .created(URI.create("/".concat(p.getId().toString())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("duration", (System.nanoTime() - startNs) + "")
                        .body(p)
                );
    }
}
