package com.gamusdev.servletreactive.performance;

import com.gamusdev.servletreactive.performance.executor.IMeterExecutor;
import com.gamusdev.servletreactive.performance.webflux.client.IWebfluxClientMeterFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MeterApplication {

	private static final String METER_EXECUTOR= "meterExecutor";

	public static void main(String[] args) throws InterruptedException {
		ConfigurableApplicationContext context = SpringApplication.run(MeterApplication.class, args);

		IMeterExecutor meterExecutor = (IMeterExecutor)context.getBean(METER_EXECUTOR);
		meterExecutor.execute();

		context.close();
	}

}
