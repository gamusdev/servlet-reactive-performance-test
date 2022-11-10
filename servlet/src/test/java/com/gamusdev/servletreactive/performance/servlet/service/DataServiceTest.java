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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;


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

    @Test
    public void getDataById(){
        // When
        Data data = Utils.createData();
        Mockito.when(dataRepository.findById(data.getId())).thenReturn(Optional.of(data));

        // Then
        Optional<Data> result = dataService.getDataById(data.getId());

        // Verify
        Assertions.assertEquals(data, result.get());
    }

    @Test
    public void postData(){
        // When
        Data data = Utils.createData();
        Mockito.when(dataRepository.save(any(Data.class))).thenReturn(data);

        // Then
        Data result = dataService.postData(data);

        // Verify
        Assertions.assertEquals(data, result);
    }

    @Test
    public void updateById(){
        // When
        Data data = Utils.createData();
        Data newData = Utils.createData();
        Data savedData = new Data(data.getId(), newData.getData());
        Mockito.when(dataRepository.findById(data.getId())).thenReturn(Optional.of(data));
        Mockito.when(dataRepository.save(savedData)).thenReturn(savedData);

        // Then
        Data result = dataService.updateById(data.getId(), newData);

        // Verify
        Assertions.assertEquals(data.getId(), result.getId());
        Assertions.assertEquals(newData.getData(), result.getData());
    }

    @Test
    public void delete(){
        // When
        Data data = Utils.createData();
        Mockito.doNothing().when(dataRepository).deleteById(data.getId());

        // Then
        dataService.delete(data.getId());

        // Verify
        verify(dataRepository).deleteById(data.getId());
    }
}
