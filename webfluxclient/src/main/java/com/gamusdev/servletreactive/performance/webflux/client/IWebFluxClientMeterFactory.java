package com.gamusdev.servletreactive.performance.webflux.client;

public interface IWebFluxClientMeterFactory {
    IWebFluxClientMeter getInstance(final String host, final String baseUri, final int counterLimit);
}
