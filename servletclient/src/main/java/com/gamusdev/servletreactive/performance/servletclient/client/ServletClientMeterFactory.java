package com.gamusdev.servletreactive.performance.servletclient.client;

import com.gamusdev.servletreactive.performance.client.common.ClientType;
import com.gamusdev.servletreactive.performance.client.common.IClientMeter;
import com.gamusdev.servletreactive.performance.client.common.IClientMeterFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementation of Interface IWebFluxClientMeterFIServletClientMeterFactoryctory
 * Provides a factory method to provide IServletClientMeter instances
 */
@Service
@Slf4j
public class ServletClientMeterFactory implements IClientMeterFactory {

    /**
     * The type of client factory
     */
    public static final ClientType TYPE = ClientType.WEBFLUX;

    public ClientType getClientType(){
        return TYPE;
    }

    /**
     * Factory method to provide IServletClientMeter instances
     * @param host The target host
     * @param baseUri The base URI
     * @param counterLimit The limit o messages received to stop the test
     * @param timeBetweenRequests Time between each request
     * @return The WebFluxClientMeter instance
     */
    @Override
    public IClientMeter getInstance(final String host, final String baseUri, final int counterLimit,
                                    final int timeBetweenRequests){
        return ServletClientMeter.getInstance( host, baseUri, counterLimit, timeBetweenRequests);
    }
}
