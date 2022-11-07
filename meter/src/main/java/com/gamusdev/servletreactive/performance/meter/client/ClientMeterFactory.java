package com.gamusdev.servletreactive.performance.meter.client;


import com.gamusdev.servletreactive.performance.client.common.IClientMeter;
import com.gamusdev.servletreactive.performance.servletclient.client.ServletClientMeter;
import com.gamusdev.servletreactive.performance.webflux.client.WebFluxClientMeter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Implementation of Interface IWebFluxClientMeterFIServletClientMeterFactoryctory
 * Provides a factory method to provide IServletClientMeter instances
 */
@Service
@Slf4j
public class ClientMeterFactory {

    /**
     * Host
     */
    @Value("${servletreactive.host}")
    private String host;

    /**
     * The base URI
     */
    @Value("${servletreactive.base_uri}")
    private String baseUri;

    /**
     * Limit of messages received to stop the test
     */
    @Value("${servletreactive.counter_limit}")
    private int counterLimit;

    /**
     * Time between each request
     */
    @Value("${servletreactive.time_between_requests}")
    private int timeBetweenRequests;

    /**
     * ClientType
     */
    @Value("${servletreactive.clientType}")
    private String clientType;

    final Supplier<IClientMeter> webFluxSupplier = () ->
            WebFluxClientMeter.getInstance( host, baseUri, counterLimit, timeBetweenRequests);

    final Supplier<IClientMeter> ServletSupplier = () ->
            ServletClientMeter.getInstance( host, baseUri, counterLimit, timeBetweenRequests);

    private enum typeEnum {
        WEBFLUX, SERVLET;
    };

    final Map<typeEnum, Supplier<IClientMeter>> generator = Map.ofEntries(
            Map.entry(typeEnum.WEBFLUX, webFluxSupplier),
            Map.entry(typeEnum.SERVLET, ServletSupplier)
    );


    /**
     * Factory method to provide IServletClientMeter instances
     * @return The WebFluxClientMeter instance
     */
    public IClientMeter getInstance(){
        if (EnumUtils.isValidEnumIgnoreCase(typeEnum.class, clientType)) {
            return generator.get(typeEnum.valueOf(clientType)).get();
        }
        else{
            throw new RuntimeException("Invalid servletreactive.clientType configuration. Valid values: {webFlux, servlet} ");
        }
    }
}
