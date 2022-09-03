package com.gamusdev.servletreactive.performance.webflux.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WebFluxClientMeterFactory implements IWebFluxClientMeterFactory {

    @Override
    public IWebFluxClientMeter getInstance(final String host, final String baseUri, final int counterLimit){
        return WebFluxClientMeter.getInstance( host, baseUri, counterLimit );
    }
}
