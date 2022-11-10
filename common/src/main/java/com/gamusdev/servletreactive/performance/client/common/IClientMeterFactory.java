package com.gamusdev.servletreactive.performance.client.common;

/**
 * Interface IClientMeterFactory
 * Provides a factory to provide IClientMeter instances
 */
public interface IClientMeterFactory {
    /**
     * Factory method to provide IServletClientMeter instances
     * @param host The target host
     * @param baseUri The base URI
     * @param counterLimit The limit o messages received to stop the test
     * @param timeBetweenRequests Time between each request
     * @return The IServletClientMeter instance
     */
    IClientMeter getInstance(final String host, final String baseUri, final int counterLimit,
                             final int timeBetweenRequests);

    ClientType getClientType();
}
