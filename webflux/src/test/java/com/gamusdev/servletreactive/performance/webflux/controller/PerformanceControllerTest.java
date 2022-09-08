package com.gamusdev.servletreactive.performance.webflux.controller;

import com.gamusdev.servletreactive.performance.webflux.WebFluxApplication;
import com.gamusdev.servletreactive.performance.webflux.exception.GenericErrorResponse;
import com.gamusdev.servletreactive.performance.webflux.model.Data;
import com.gamusdev.servletreactive.performance.webflux.service.DataService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

/**
 * DataControllerTest
 * Unitary testing for DataController
 */
@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = PerformanceController.class)
@ContextConfiguration(classes = WebFluxApplication.class)
public class PerformanceControllerTest {

    private static final String BASE_URI = "/api/v1/performance/";

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private ApplicationContext context;

    @MockBean
    private DataService dataService;

    /**
     * GET / returns 200 OK
     */
    @Test
    public void getAllData() {
        // When
        final List<Data> allData = List.of(
                Data.builder().data( RandomStringUtils.randomAlphanumeric(10)).id(RandomUtils.nextInt()).build(),
                Data.builder().data( RandomStringUtils.randomAlphanumeric(10)).id(RandomUtils.nextInt()).build()
        );
        Mockito.when(dataService.getAllData()).thenReturn(Flux.fromIterable(allData));

        // Then & Verify
        webClient.get().uri( BASE_URI ).exchange()
                .expectStatus().isOk()
                .expectBodyList(Data.class)
                .consumeWith(r -> {
                    List<Data> resultList = r.getResponseBody();
                    Assertions.assertNotNull(resultList );

                    // Test that the result contains all the initial records
                    // Data does not implement equals, so I have to test any element
                    resultList.forEach( res ->
                                Assertions.assertTrue( allData.stream().
                                    anyMatch( d -> d.getId().equals(res.getId()) && d.getData().equals(res.getData()) )
                                )
                            );

                    Assertions.assertEquals(allData.size(), resultList.size());
                });
    }

    /**
     * GET /{id} returns 200 OK if found
     */
    @Test
    public void getDataById() {
        // When
        final Data data = Data.builder().data( RandomStringUtils.randomAlphanumeric(10)).id(RandomUtils.nextInt()).build();
        Mockito.when(dataService.getDataById(data.getId())).thenReturn(Mono.just(data));

        // Then & Verify
        webClient.get().uri(BASE_URI.concat("/{id}"), Collections.singletonMap("id", data.getId()))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Data.class)
                .consumeWith(r -> {
                    Data resultData = r.getResponseBody();
                    Assertions.assertNotNull(resultData);
                    Assertions.assertEquals(data.getId(), resultData.getId());
                });
    }

    /**
     * GET /{id} returns 404 Not Found
     */
    @Test
    public void getDataByIdNotFound() {
        // When
        Mockito.when(dataService.getDataById(any())).thenReturn(Mono.empty());

        // Then & Verify
        webClient.get().uri(BASE_URI.concat("/{id}"), Collections.singletonMap("id", 1))
                .exchange()
                .expectStatus().isNotFound();
    }

    /**
     * POST / returns 201 CREATED with DTO & location header
     */
    @Test
    public void postData() {
        // When
        final Data data = Data.builder().data( RandomStringUtils.randomAlphanumeric(10)).id(RandomUtils.nextInt()).build();
        Mockito.when(dataService.postData(any(String.class))).thenReturn(Mono.just(data));

        // Then & Verify
        webClient.post().uri( BASE_URI )
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(data), Data.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectHeader().location(BASE_URI.concat(data.getId().toString()))
                .expectBody(Data.class)
                .consumeWith(r -> {
                    Assertions.assertNotNull(r.getResponseBody());
                    Assertions.assertEquals(data.getData(), r.getResponseBody().getData());
                } );
    }

    /**
     * POST /{id} must always return 404 NOT FOUND
     */
    @Test
    public void postDataById() {
        // When
        final Data data = Data.builder().data( RandomStringUtils.randomAlphanumeric(10)).id(RandomUtils.nextInt()).build();

        // Then & Verify
        webClient.post().uri( BASE_URI.concat( "/{id}"), Collections.singletonMap("id", data.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(data), Data.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    /**
     * PUT / must always return 404 NOT FOUND
     */
    @Test
    public void putDataById() {
        // When
        final Data data = Data.builder().data( RandomStringUtils.randomAlphanumeric(10)).id(RandomUtils.nextInt()).build();

        // Then & Verify
        webClient.put().uri( BASE_URI )
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(data), Data.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    /**
     * PUT /{id} return 200 OK
     */
    @Test
    public void updateById() {
        // When
        final Data updatedData = Data.builder().data( RandomStringUtils.randomAlphanumeric(10)).id(RandomUtils.nextInt()).build();
        Mockito.when(dataService.updateById(eq(updatedData.getId()), any(Data.class))).thenReturn(Mono.just(updatedData));

        // Then & Verify
        webClient.put().uri( BASE_URI.concat( "/{id}"), Collections.singletonMap("id", updatedData.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedData), Data.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Data.class)
                .consumeWith(r -> {
                    Assertions.assertNotNull(r.getResponseBody());
                    Assertions.assertEquals(updatedData.getId(), r.getResponseBody().getId());
                    Assertions.assertEquals(updatedData.getData(), r.getResponseBody().getData());
                } );
    }

    /**
     * PUT / return 404 NOT FOUND
     */
    @Test
    public void updateByIdNotFound () {
        // When
        final Data updatedData = Data.builder().data( RandomStringUtils.randomAlphanumeric(10)).id(RandomUtils.nextInt()).build();
        Mockito.when(dataService.updateById(any(), any())).thenReturn(Mono.empty());

        // Then & Verify
        webClient.put().uri( BASE_URI.concat( "/{id}"), Collections.singletonMap("id", 1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedData), Data.class)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .isEmpty();
    }

    /**
     * DELETE / must always return 404 NOT FOUND
     */
    @Test
    public void deleteData() {
        // When
        final Data data = Data.builder().data( RandomStringUtils.randomAlphanumeric(10)).id(RandomUtils.nextInt()).build();

        // Then & Verify
        webClient.delete().uri( BASE_URI )
                .exchange()
                .expectStatus().isNotFound();
    }

    /**
     * DELETE /{id} returns 204 NO CONTENT
     */
    @Test
    public void deleteDataById() {
        // When
        final Data deletedData = Data.builder().data( RandomStringUtils.randomAlphanumeric(10)).id(RandomUtils.nextInt()).build();
        Mockito.when(dataService.delete(deletedData.getId())).thenReturn(Mono.empty());

        // Then & Verify
        webClient.delete()
                .uri(BASE_URI.concat( "/{id}"), Collections.singletonMap("id", deletedData.getId()))
                .exchange()
                .expectStatus().isNoContent()
                .expectBody()
                .isEmpty();
    }

    @Test
    public void deleteThrows() {
        // When
        final String message = "message";
        final Data deletedData = Data.builder().data( RandomStringUtils.randomAlphanumeric(10)).id(RandomUtils.nextInt()).build();
        Mockito.when(dataService.delete(deletedData.getId())).thenThrow(new RuntimeException(message));

        // Then & Verify
        webClient.delete()
                .uri(BASE_URI.concat( "/{id}"), Collections.singletonMap("id", deletedData.getId()))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(GenericErrorResponse.class)
                .consumeWith(e -> {
                    Assertions.assertNotNull(e.getResponseBody());
                    Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, e.getResponseBody().getStatus());
                } );
    }
}
