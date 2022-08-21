package com.gamusdev.servletreactive.performance.webflux.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Data {
    private Integer id;
    private String data;
}
