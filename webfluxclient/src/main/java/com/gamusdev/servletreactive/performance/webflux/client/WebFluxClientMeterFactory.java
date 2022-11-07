package com.gamusdev.servletreactive.performance.webflux.client;

import com.gamusdev.servletreactive.performance.client.common.IClientMeter;
import com.gamusdev.servletreactive.performance.client.common.IClientMeterFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementation of Interface IWebFluxClientMeterFactory
 * Provides a factory method to provide IWebFluxClientMeter instances
 */
@Service
@Slf4j
public class WebFluxClientMeterFactory implements IClientMeterFactory {

    /**
     * Factory method to provide IWebFluxClientMeter instances
     * @param host The target host
     * @param baseUri The base URI
     * @param counterLimit The limit o messages received to stop the test
     * @param timeBetweenRequests Time between each request
     * @return The WebFluxClientMeter instance
     */
    @Override
    public IClientMeter getInstance(final String host, final String baseUri, final int counterLimit,
                                    final int timeBetweenRequests){
        return WebFluxClientMeter.getInstance( host, baseUri, counterLimit, timeBetweenRequests);
    }
}
