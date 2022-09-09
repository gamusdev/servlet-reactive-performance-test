package com.gamusdev.servletreactive.performance.webflux.client;

/**
 * Interface IWebFluxClientMeterFactory
 * Provides a factory to provide IWebFluxClientMeter instances
 */
public interface IWebFluxClientMeterFactory {

    /**
     * Factory method to provide IWebFluxClientMeter instances
     * @param host The target host
     * @param baseUri The base URI
     * @param counterLimit The limit o messages received to stop the test
     * @param timeBetweenRequests Time between each request
     * @return The WebFluxClientMeter instance
     */
    IWebFluxClientMeter getInstance(final String host, final String baseUri, final int counterLimit,
                                    final int timeBetweenRequests);
}
