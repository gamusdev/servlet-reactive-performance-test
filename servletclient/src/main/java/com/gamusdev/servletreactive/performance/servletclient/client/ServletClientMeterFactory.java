package com.gamusdev.servletreactive.performance.servletclient.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementation of Interface IWebFluxClientMeterFIServletClientMeterFactoryctory
 * Provides a factory method to provide IServletClientMeter instances
 */
@Service
@Slf4j
public class ServletClientMeterFactory implements IServletClientMeterFactory{
    /**
     * Factory method to provide IServletClientMeter instances
     * @param host The target host
     * @param baseUri The base URI
     * @param counterLimit The limit o messages received to stop the test
     * @param timeBetweenRequests Time between each request
     * @return The WebFluxClientMeter instance
     */
    @Override
    public IServletClientMeter getInstance(final String host, final String baseUri, final int counterLimit,
                                           final int timeBetweenRequests){
        return ServletClientMeter.getInstance( host, baseUri, counterLimit, timeBetweenRequests);
    }
}
