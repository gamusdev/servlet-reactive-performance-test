package com.gamusdev.servletreactive.performance.servlet.model;

import lombok.*;
import org.springframework.data.annotation.Id;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Data {

    @Id
    private Integer id;
    private String data;
}
