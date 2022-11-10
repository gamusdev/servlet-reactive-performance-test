# servlet-reactive-performance-test

Spring Reactive is supposed to be able to manage resources better than Servlets. This does not mean that the time between
calls will be lower with the Reactive stack, but it can handle more request with fewer resources.

Typically, web servers using Servlets has a pool of (many) thread, and when these threads execute calls to external systems 
(another api, a database access...), the threads wait for the responses.

On other hand, web servers that implement a reactive core have few threads. When these threads execute this calls
to external systems, the threads are released and can manage other requests.

The purpose of this project is to test this concept, with a simple and small reactive application VS the same application 
written with Servlets.

This project will have:
1) A WebFlux application and a Servlet application.
2) A WebClient and a RestTemplate client.
3) A Meter project, that will use the clients to measure the requests.

# WebFlux

This is a simple CRUD WebFlux application. The Rest API follows the most common best practices.

To maintain the test simple, an H2 database is used. This database is written in Java and loaded in memory.

The project uses Spring Data to access to the database.

# To tests the controller:

To test easily the application, you can use curl:

```
curl --header "Content-Type: application/json" \
  --request POST \
  --data '{"data":"xyz"}' \
  http://localhost:3000/api/login

(-H is short for --header, -d for --data)
```
```
curl -i -X POST -H 'Content-Type: application/json' \
   -d '{"data":"value1"}' \
   http://localhost:8090/api/v1/performance/ 
```
```
curl -i http://localhost:8090/api/v1/performance/ 
```
```
curl -i http://localhost:8090/api/v1/performance/0
```
```
curl -i http://localhost:8090/api/v1/performance/1 
```

# WebFlux Client

The client uses a Springboot WebClient. The WebClient is encapsulated in the WebFluxClientMeter class. 
This WebFluxClientMeter is a Singleton obtained with the Factory pattern. 
The IWebFluxClientMeter defines the supported 
operations, and encapsulates the counters to get the performance measure for the Reactive test.

Also, it supports as parameter a generic Consumer that could consume the responses. This consumer will be implemented 
in the Meter project.

The main method in this project is written as an example of use, and enables to use this project directly. 
But in our test, the execution of the client will be done by the Meter application.

# Servlet

It is a CRUD similar to the WebFlux version. The Rest API also follows the most common best practices.

# Servlet Client

This client is a RestTemplate wrapped in the ServletClientMeter class (it is also another Singleton obtained with the 
Factory pattern).
Again, the ServletClientMeter defines the supported
operations, and encapsulates the counters to get the performance measure for the Servlet test.

And finally, it also supports the generic Consumer, used in the Meter project.

# Meter

The Meter project is the one that will execute the clients and capture the measured results.
Basically, it is a big map implemented in DataManager and an executor (MeterExecutor), that executes the calls to the 
Rest Api using the clients.

The dataManager is passed as a Consumer to the clients, and saves all the information from the responses.

An active waiting is implemented in each step of the test to maintain things easier.

Finally, all the information is shown in the console.

As note, the GeneralClientMeterFactory is the class that manages the client type created (Servlet or WebFlux). This
class follows the SOLID principles, and uses the factory methods of the clients to create the required client 
(in a lazy mode, only the requested client is created).


# Execution for the WebFlux test

To execute the reactive server, you need to configure the environment variables, and then execute the jar file:
```
env server.port=8090 bash
env spring.datasource.platform=h2 bash
env spring.r2dbc.url="r2dbc:h2:mem:///gamusdev;DB_CLOSE_DELAY=-1" bash
env  | grep port
env  | grep spring

java -jar target/webflux-0.0.1.jar
```

And the meter:
```
env logging.level.io=INFO bash
env logging.level.reactor=INFO bash
env logging.level.org=INFO bash
env logging.level.com.gamusdev=INFO bash

# Do not start the embedded server
env spring.main.web-application-type="none"

env servletreactive.clientType="webFlux"
env servletreactive.host="http://{host}:8090" bash
#env servletreactive.host="http://localhost:8090" bash
env servletreactive.base_uri="/api/v1/performance/" bash

# The test size parameters
env servletreactive.counter_limit=1000 bash
env servletreactive.time_between_requests=1 bash

env  | grep logging
env  | grep servletreactive

java -jar target/meter-0.0.3.jar
```

# Execution for the Servlet test

To execute the reactive server, you need to configure the environment variables, and then execute the jar file:
```
env server.port=8090 bash
env spring.datasource.platform=h2 bash
env spring.r2dbc.url="r2dbc:h2:mem:///gamusdev;DB_CLOSE_DELAY=-1" bash
env  | grep port
env  | grep spring

java -jar target/webflux-0.0.1.jar
```

And the meter:
```
env logging.level.io=INFO bash
env logging.level.reactor=INFO bash
env logging.level.org=INFO bash
env logging.level.com.gamusdev=INFO bash

# Do not start the embedded server
env spring.main.web-application-type="none"

env servletreactive.clientType="servlet"
env servletreactive.host="http://{host}:8090" bash
#env servletreactive.host="http://localhost:8090" bash
env servletreactive.base_uri="/api/v1/performance/" bash

# The test size parameters
env servletreactive.counter_limit=1000 bash
env servletreactive.time_between_requests=1 bash

env  | grep logging
env  | grep servletreactive

java -jar target/meter-0.0.3.jar
```

## Author
http://www.gamusdev.com

dramirez@gamusdev.com