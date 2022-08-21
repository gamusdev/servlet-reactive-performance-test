package com.gamusdev.servletreactive.performance.webflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class WebfluxClientTest {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(WebfluxClientTest.class, args);

        context.close();
    }
}
