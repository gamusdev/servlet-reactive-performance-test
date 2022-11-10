package com.gamusdev.servletreactive.performance.servlet.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamusdev.servletreactive.performance.servlet.model.Data;
import com.gamusdev.servletreactive.performance.servlet.service.DataService;
import com.gamusdev.servletreactive.performance.servlet.utils.Utils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;


@WebMvcTest(controllers = PerformanceController.class)
public class PerformanceControllerTest {

    private static final String CONTROLLER_URI = "/api/v1/performance";
    private static final String DURATION = "duration";

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DataService dataService;

    @Test
    public void getAllData() throws Exception {
        // When
        List<Data> allData = List.of(Utils.createData(), Utils.createData(), Utils.createData());
        Mockito.when(dataService.getAllData()).thenReturn(allData);

        // Then && Verify
        this.mockMvc.perform(MockMvcRequestBuilders.get(CONTROLLER_URI))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().exists(DURATION))
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(allData)));
    }

    @Test
    public void getDataById() throws Exception {
        // When
        Data data = Utils.createData();
        Mockito.when(dataService.getDataById(data.getId())).thenReturn(Optional.of(data));

        // Then && Verrify
        this.mockMvc.perform(MockMvcRequestBuilders.get(CONTROLLER_URI + "/{id}", data.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().exists(DURATION))
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(data)));
    }

    @Test
    public void getDataById404() throws Exception {
        // When
        Data data = Utils.createData();
        Mockito.when(dataService.getDataById(data.getId())).thenReturn(Optional.empty());

        // Then && Verify
        this.mockMvc.perform(MockMvcRequestBuilders.get(CONTROLLER_URI + "/{id}", data.getId()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void postData() throws Exception {
        // When
        Data data = Utils.createData();
        Mockito.when(dataService.postData(any(Data.class))).thenReturn(data);

        // Then && Verify
        this.mockMvc.perform(
                MockMvcRequestBuilders.post(CONTROLLER_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(data)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists(DURATION))
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(data)));
    }

    @Test
    public void postData404() throws Exception {
        // When
        Data data = Utils.createData();

        // Then && Verify
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post(CONTROLLER_URI + "/{id}", data.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(data)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void updateData404() throws Exception {
        // When
        Data data = Utils.createData();

        // Then && Verify
        this.mockMvc.perform(
                        MockMvcRequestBuilders.put(CONTROLLER_URI)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(data)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void updateData() throws Exception {
        // When
        Data data = Utils.createData();
        Mockito.when(dataService.updateById(eq(data.getId()), any(Data.class))).thenReturn(data);

        // Then && Verify
        this.mockMvc.perform(
                        MockMvcRequestBuilders.put(CONTROLLER_URI + "/{id}", data.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(data)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists(DURATION))
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(data)));
    }

    @Test
    public void deleteData404() throws Exception {
        // When
        Data data = Utils.createData();

        // Then && Verify
        this.mockMvc.perform(
                        MockMvcRequestBuilders.delete(CONTROLLER_URI)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(data)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void deleteData() throws Exception {
        // When
        Data data = Utils.createData();

        // Then && Verify
        this.mockMvc.perform(
                        MockMvcRequestBuilders.delete(CONTROLLER_URI + "/{id}", data.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(data)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.header().exists(DURATION));
    }
}
