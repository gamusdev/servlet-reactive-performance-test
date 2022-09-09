package com.gamusdev.servletreactive.performance.webflux.service;

import com.gamusdev.servletreactive.performance.webflux.model.Data;
import com.gamusdev.servletreactive.performance.webflux.repository.DataRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.concurrent.ThreadLocalRandom;

import static org.mockito.ArgumentMatchers.any;

/**
 * DataServiceTest
 * Unitary testing for DataService
 */
@ExtendWith(SpringExtension.class)
public class DataServiceTest {

    @Mock
    private DataRepository dataRepository;

    @InjectMocks
    private DataService dataService;

    /**
     * Test DataService.getAllData with a fixed number of records
     */
    @Test
    public void getThreeRecords() {
        // When
        final Flux<Data> repositoryData = Flux.just(
                Data.builder().data( RandomStringUtils.randomAlphanumeric(10)).id(RandomUtils.nextInt()).build(),
                Data.builder().data( RandomStringUtils.randomAlphanumeric(10)).id(RandomUtils.nextInt()).build(),
                Data.builder().data( RandomStringUtils.randomAlphanumeric(10)).id(RandomUtils.nextInt()).build()
        );
        Mockito.when(dataRepository.findAll()).thenReturn(repositoryData);

        // Then
        final Flux<Data> returnedData = dataService.getAllData();

        // Verify
        StepVerifier.create(returnedData)
                .thenConsumeWhile(item -> repositoryData.toStream().anyMatch(f -> f.equals(item)) )
                .verifyComplete();

    }

    /**
     * Test DataService.getAllData with a random number of records
     */
    @Test
    public void getNRecords() {
        // When
        final int elements = ThreadLocalRandom.current().nextInt(100);
        final Flux<Data> repositoryData = Flux.range(0, elements)
                .map(i -> Data.builder().data( RandomStringUtils.randomAlphanumeric(10)).id(RandomUtils.nextInt()).build());
        Mockito.when(dataRepository.findAll()).thenReturn(repositoryData);

        // Then
        final Flux<Data> returnedRecords = dataService.getAllData();

        // Verify
        StepVerifier.create(returnedRecords)
                .expectNextCount( elements )
                .verifyComplete();
    }

    /**
     * Test DataService.getDataById with a Random Data
     */
    @Test
    public void getDataById() {
        // When
        final int id = ThreadLocalRandom.current().nextInt();
        final Mono<Data> repositoryData = Mono.just( Data.builder().data( RandomStringUtils.randomAlphanumeric(10)).id(RandomUtils.nextInt()).build() );
        Mockito.when(dataRepository.findById(id)).thenReturn(repositoryData);

        // Then
        final Mono<Data> returnedData = dataService.getDataById(id);

        // Verify
        StepVerifier.create(
                returnedData.zipWith(repositoryData))
                .assertNext(item -> Assertions.assertEquals(item.getT1(), item.getT2()))
                .verifyComplete();
    }

    /**
     * Test DataService.postData with a Random Data
     */
    @Test
    public void postData() {
        // When
        final Data savedData = Data.builder().data( RandomStringUtils.randomAlphanumeric(10)).id(RandomUtils.nextInt()).build();
        Mockito.when(dataRepository.save(any())).thenReturn(Mono.just(savedData));

        // Then
        final Mono<Data> returnedData = dataService.postData(savedData);

        // Verify
        StepVerifier.create( returnedData.zipWith(returnedData) )
                .assertNext(item -> Assertions.assertEquals(item.getT1(), item.getT2()))
                .verifyComplete();
    }

    /**
     * Test DataService.updateById with Random Records
     */
    @Test
    public void updateById() {
        // When
        final Data oldData = Data.builder().data( RandomStringUtils.randomAlphanumeric(10)).id(RandomUtils.nextInt()).build();
        final Data updatedData = Data.builder().data( RandomStringUtils.randomAlphanumeric(10)).id(RandomUtils.nextInt()).build();
        ReflectionTestUtils.setField(updatedData, "id", oldData.getId());

        final Data expectedResult = Data.builder().data( updatedData.getData() ).id( oldData.getId() ).build();
        Mockito.when(dataRepository.findById(oldData.getId())).thenReturn(Mono.just(oldData));
        Mockito.when(dataRepository.save(any(Data.class))).thenReturn(Mono.just(expectedResult));

        // Then
        final Mono<Data> returnedData = dataService.updateById(updatedData.getId(), updatedData);

        // Verify
        StepVerifier.create( returnedData )
                .assertNext(item -> Assertions.assertEquals(oldData.getId(), item.getId()) )
                .verifyComplete();
    }

    /**
     * Test DataService.delete
     */
    @Test
    public void delete() {
        // When
        final int id = ThreadLocalRandom.current().nextInt();
        Mockito.when(dataRepository.deleteById(id)).thenReturn(Mono.empty());

        // Then
        final Mono<Void> result = dataService.delete(id);

        // Verify
        StepVerifier.create( result ).verifyComplete();
    }
}
