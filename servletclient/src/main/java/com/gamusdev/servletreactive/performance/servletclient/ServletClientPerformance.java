package com.gamusdev.servletreactive.performance.servletclient;

import com.gamusdev.servletreactive.performance.servletclient.client.IServletClientMeter;
import com.gamusdev.servletreactive.performance.servletclient.client.IServletClientMeterFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * SpringBoot main class to execute the test
 * It is written as example of use, but instead of this class, the package meter should be used.
 * This class is not used in the execution of the performance test. It is just an example of use.
 */

@SpringBootApplication
@Slf4j
public class ServletClientPerformance {

	private static final String HOST = "http://localhost:8090";
	private static final String BASE_URI = "/api/v1/performance/";
	private static final String WEB_FLUX_CLIENT_METER_FACTORY= "servletClientMeterFactory";

	private static final int COUNTER_LIMIT = 100;
	private static final int TIME_BETWEEN_REQUESTS = 1;

	public static void main(String[] args) throws InterruptedException {
		ConfigurableApplicationContext context = SpringApplication.run(ServletClientPerformance.class, args);

		IServletClientMeterFactory factory = (IServletClientMeterFactory)context.getBean(WEB_FLUX_CLIENT_METER_FACTORY);
		executeServletClient(factory.getInstance(HOST, BASE_URI, COUNTER_LIMIT, TIME_BETWEEN_REQUESTS));

		context.close();
	}
	/**
	 * Example of execution
	 * @param client The IWebFluxClientMeter used
	 * @throws InterruptedException Exception
	 */
	private static void executeServletClient(IServletClientMeter client) throws InterruptedException {
		log.info("Starting Servlet test...");
		long start = System.nanoTime();

		client.postData(d -> log.info(d.toString()));
		while(client.getCounterPost() < COUNTER_LIMIT) {
			Thread.sleep(100);
		}

		client.getAllData(d -> log.info(d.toString()));
		while(client.getCounterGetAll() < 1) {
			Thread.sleep(100);
		}

		client.putData(d -> log.info(d.toString()));
		while(client.getCounterPut() < COUNTER_LIMIT) {
			Thread.sleep(100);
		}

		client.getData(d -> log.info(d.toString()));
		while(client.getCounterGet() < COUNTER_LIMIT) {
			Thread.sleep(100);
		}

		client.deleteData(d -> log.info(d.toString()));
		while(client.getCounterDelete() < COUNTER_LIMIT) {
			Thread.sleep(100);
		}

		long end = System.nanoTime();
		log.info("---------------------------------------------------------");
		//log.info("Servlet test is finished. Duration=" + (end-start) + " ns");
		log.info("Servlet test is finished. Duration=" + (end-start)/1_000_000 + " ms");
		log.info("---------------------------------------------------------");

	}
}
