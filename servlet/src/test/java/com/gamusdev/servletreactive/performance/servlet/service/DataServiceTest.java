package com.gamusdev.servletreactive.performance.servlet.service;

import com.gamusdev.servletreactive.performance.servlet.model.Data;
import com.gamusdev.servletreactive.performance.servlet.repository.DataRepository;
import com.gamusdev.servletreactive.performance.servlet.utils.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
public class DataServiceTest {

    @Mock
    private DataRepository dataRepository;

    @InjectMocks
    private DataService dataService;

    @Test
    public void getAllData(){
        // When
        List<Data> allData = List.of(Utils.createData(), Utils.createData(), Utils.createData());
        Mockito.when(dataRepository.findAll()).thenReturn(allData);

        // Then
        List<Data> result = dataService.getAllData();

        // Verify
        Assertions.assertEquals(allData, result);
    }
}
