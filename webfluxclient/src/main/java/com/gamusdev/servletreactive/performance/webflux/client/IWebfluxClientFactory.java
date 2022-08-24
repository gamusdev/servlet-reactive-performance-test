package com.gamusdev.servletreactive.performance.webflux.client;

public interface IWebfluxClientFactory {
    IWebfluxClient getInstance( final String host, final String baseUri, final int counterLimit);
}
