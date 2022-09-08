package com.gamusdev.servletreactive.performance.webflux.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementation of Interface IWebFluxClientMeterFactory
 * Provides a factory method to provide IWebFluxClientMeter instances
 */
@Service
@Slf4j
public class WebFluxClientMeterFactory implements IWebFluxClientMeterFactory {

    /**
     * Factory method to provide IWebFluxClientMeter instances
     * @param host The target host
     * @param baseUri The base URI
     * @param counterLimit The limit o messages received to stop the test
     * @return The WebFluxClientMeter instance
     */
    @Override
    public IWebFluxClientMeter getInstance(final String host, final String baseUri, final int counterLimit){
        return WebFluxClientMeter.getInstance( host, baseUri, counterLimit );
    }
}
