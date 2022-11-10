package com.gamusdev.servletreactive.performance.servlet.utils;

import com.gamusdev.servletreactive.performance.servlet.model.Data;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.concurrent.ThreadLocalRandom;

public class Utils {

    public static Data createData() {
        return Data.builder()
                .id(ThreadLocalRandom.current().nextInt())
                .data(RandomStringUtils.randomAlphanumeric(10))
                .build();
    }
}
