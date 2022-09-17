# servlet-reactive-performance-test

Spring Reactive is supposed to be able to manage resources better than Servlets. This does not mean that the time between
calls will be lower with the Reactive stack, but it can handle more request with fewer resources.

Typically, web servers using Servlets has a pool of (many) thread, and when these threads execute calls to external systems 
(another api, a database access...), the threads wait for the responses.

On other hand, web servers that implement a reactive core has few threads. When these threads execute this calls
to external systems, the threads are released and can manage other requests.

The purpose of this project is to test this, with a small reactive application VS the same application written with Servlets.

This project will have:
1) A WebFlux application and a Servlet application.
2) A WebClient and a RestTemplate client.
3) A Meter project, that will use the clients to measure the requests.

---
Note: At this moment, only the Reactive projects are written.
---
---

# WebFlux

This is a simple CRUD WebFlux application.

To maintain the test simple, an H2 database is used. This database is written in Java and loaded in memory.

The project uses Spring Data to access to the database.

# To tests controller:

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
