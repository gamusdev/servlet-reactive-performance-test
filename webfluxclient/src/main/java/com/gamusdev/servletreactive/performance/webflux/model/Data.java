package com.gamusdev.servletreactive.performance.webflux.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Simple Data class
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Data {
    /**
     * Id of the records
     */
    private Integer id;

    /**
     * Payload
     */
    private String data;
}
