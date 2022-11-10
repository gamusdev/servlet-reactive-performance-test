package com.gamusdev.servletreactive.performance.meter.client;

import com.gamusdev.servletreactive.performance.client.common.ClientType;
import com.gamusdev.servletreactive.performance.client.common.IClientMeter;
import com.gamusdev.servletreactive.performance.client.common.IClientMeterFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Implementation of Interface IWebFluxClientMeterFIServletClientMeterFactoryctory
 * Provides a factory method to provide IServletClientMeter instances
 */
@Service
@Slf4j
public class GeneralClientMeterFactory implements IGeneralClientMeterFactory{

    /**
     * Host
     */
    @Value("${servletreactive.host}")
    private String host;

    /**
     * The base URI
     */
    @Value("${servletreactive.base_uri}")
    private String baseUri;

    /**
     * Limit of messages received to stop the test
     */
    @Value("${servletreactive.counter_limit}")
    private int counterLimit;

    /**
     * Time between each request
     */
    @Value("${servletreactive.time_between_requests}")
    private int timeBetweenRequests;

    /**
     * ClientType
     */
    @Value("${servletreactive.clientType}")
    private String clientType;

    /**
     * Map with all the implementations of the factories
     */
    final Map<ClientType, Supplier<IClientMeter>> generator;

    /**
     * List of IClientMeter Factories
     */
    private List<IClientMeterFactory> factories;

    /**
     * Constructor
     * This constructor configures the factories property with all the enabled
     * factories.
     * With these factories the load of the Client Meters is lazy loaded, only is
     * loaded when needed.
     * @param factories The list of all the IClientMeterFactory factories
     */
    @Autowired
    public GeneralClientMeterFactory(List<IClientMeterFactory> factories)    {
        generator = new HashMap<>();
        this.factories = factories;
        for(IClientMeterFactory factory: factories){
            generator.put(
                factory.getClientType(),
                () -> factory.getInstance( host, baseUri, counterLimit, timeBetweenRequests)
            );
        }
    }

    /**
     * Factory method to provide IServletClientMeter instances
     * @return The WebFluxClientMeter instance
     */
    @Override
    public IClientMeter getInstance(){
        if (EnumUtils.isValidEnumIgnoreCase(ClientType.class, clientType)) {
            return generator.get(ClientType.valueOf(clientType.toUpperCase())).get();
        }
        else{
            throw new RuntimeException("Invalid servletreactive.clientType configuration. Valid values: {webFlux, servlet} ");
        }
    }
}
