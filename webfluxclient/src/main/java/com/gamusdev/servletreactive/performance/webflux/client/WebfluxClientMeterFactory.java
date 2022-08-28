package com.gamusdev.servletreactive.performance.webflux.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WebfluxClientMeterFactory implements IWebfluxClientMeterFactory {
    public IWebfluxClientMeter getInstance(final String host, final String baseUri, final int counterLimit){
        return WebfluxClientMeter.getInstance( host, baseUri, counterLimit );
    }
}
