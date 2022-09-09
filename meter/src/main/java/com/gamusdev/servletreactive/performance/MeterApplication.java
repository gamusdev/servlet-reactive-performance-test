package com.gamusdev.servletreactive.performance;

import com.gamusdev.servletreactive.performance.executor.IMeterExecutor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * SpringBoot main class to execute the test
 */
@SpringBootApplication
public class MeterApplication {

	private static final String METER_EXECUTOR= "meterExecutor";

	/**
	 * Main method
	 * @param args args
	 * @throws InterruptedException InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		ConfigurableApplicationContext context = SpringApplication.run(MeterApplication.class, args);

		// Get the meter executor & execute
		IMeterExecutor meterExecutor = (IMeterExecutor)context.getBean(METER_EXECUTOR);
		meterExecutor.execute();

		// Once executed, exit the application
		context.close();
	}

}
