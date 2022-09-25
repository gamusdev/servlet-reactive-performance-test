package com.gamusdev.servletreactive.performance.servlet.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Data {

    @Id
    private Integer id;
    private String data;
}
