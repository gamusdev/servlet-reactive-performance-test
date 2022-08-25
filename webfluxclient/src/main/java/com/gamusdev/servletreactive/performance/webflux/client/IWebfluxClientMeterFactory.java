package com.gamusdev.servletreactive.performance.webflux.client;

public interface IWebfluxClientMeterFactory {
    IWebfluxClientMeter getInstance(final String host, final String baseUri, final int counterLimit);
}
