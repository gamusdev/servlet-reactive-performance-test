package com.gamusdev.servletreactive.performance.servlet.controller;

import com.gamusdev.servletreactive.performance.servlet.model.Data;
import com.gamusdev.servletreactive.performance.servlet.service.DataService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/performance")
@Log4j2
public class PerformanceController {

    private static final String CONTROLLER_URI = "/api/v1/performance/";
    private static final String DURATION = "duration";

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

    /**
     * Get all the data.
     * The duration is returned on a header called "duration".
     * @return All the data with the duration header
     */
    @GetMapping
    public ResponseEntity<List<Data>> getAllData() {
        final long startNs = System.nanoTime();
        List<Data> allData = this.dataService.getAllData();
        return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(DURATION, (System.nanoTime() - startNs) + "")
                        .body(allData);
    }

    /**
     * Get the data by identifier, or 404 if not fount.
     * The duration is returned on a header called "duration".
     * @param id identifier
     * @return The data by id with the duration header
     */
    @GetMapping("/{id}")
    public ResponseEntity<Data> getDataById(@PathVariable final Integer id) {
        final long startNs = System.nanoTime();
        return this.dataService.getDataById(id).
                map((d ->
                        ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(DURATION, (System.nanoTime() - startNs) + "")
                        .body( d ))).
                orElse(ResponseEntity.notFound().build());
    }

    /**
     * Save a new record.
     * The duration is returned on a header called "duration".
     * @param data New data.
     * @return the saved data with the duration.
     */
    @PostMapping
    public ResponseEntity<Data> postData(@RequestBody final Data data) {
        final long startNs = System.nanoTime();
        Data result = this.dataService.postData(data);
        return ResponseEntity
                        .created(URI.create(CONTROLLER_URI.concat(result.getId().toString())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(DURATION, (System.nanoTime() - startNs) + "")
                        .body(result);
    }

    /**
     * POST /{id} must always return 404 NOT FOUND,
     * @param data The data
     * @return Mono<ResponseEntity<Page>> Not found
     */
    @PostMapping("/{id}")
    public ResponseEntity<Data> postPageById(@RequestBody final Data data) {
        return ResponseEntity.notFound().build();
    }

    /**
     * PUT /pages must always return 404 NOT FOUND,
     * Note Best practices: Also, the PUT /Pages also could modify all the collection
     * @param data The data
     * @return Mono<ResponseEntity<Page>> Not found
     */
    @PutMapping
    public ResponseEntity<Data> update(@RequestBody final Data data) {
        return ResponseEntity.notFound().build();
    }

    /**
     * PUT /{id} returns 200 if modified, 404 if not found.
     * The duration is returned on a header called "duration".
     * @param id the id to modify
     * @param data the new data
     * @return Mono<ResponseEntity<Page>>  with the updated data
     */
    @PutMapping("/{id}")
    public ResponseEntity<Data> updateById(
            @PathVariable Integer id, @RequestBody final Data data) {
        final long startNs = System.nanoTime();
        Data result = dataService.updateById(id, data);
        return ResponseEntity
                        .created(URI.create("/".concat(result.getId().toString())))
                        .header(DURATION, (System.nanoTime() - startNs) + "")
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(result);
    }

    /**
     * DELETE / must always return 404 NOT FOUND.
     * Note Best practices: Also, the DELETE / also could delete all the collection
     * @return Mono<ResponseEntity<Void>> with 404 NOT FOUND
     */
    @DeleteMapping
    public ResponseEntity<Void> delete() {
        return ResponseEntity.notFound().build();
    }

    /**
     * Delete /{id} Deletes de data. Returns always 204 NO CONTENT.
     * The duration is returned on a header called "duration".
     * Note: Rest best practices returns 404 if not found, but repository.deleteById returns Mono<Void>
     *     So, it is needed to search before delete. This step is skipped, because it is an idempotent op.
     * @param id identifier to delete
     * @return ResponseEntity<Void>>
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable final Integer id) {
        final long startNs = System.nanoTime();
        MultiValueMap<String, String> headers = new LinkedMultiValueMap(
                Map.of(DURATION, List.of((System.nanoTime() - startNs) + "")));
        dataService.delete(id);
        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

}
