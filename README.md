# servlet-reactive-performance-test

# To tests controller:

curl --header "Content-Type: application/json" \
  --request POST \
  --data '{"username":"xyz","password":"xyz"}' \
  http://localhost:3000/api/login

(-H is short for --header, -d for --data)


curl -i -X POST -H 'Content-Type: application/json' \
   -d '{"key1":"value1","key2":"value2"}' \
   http://localhost:8090/api/v1/performance/ 

curl -i http://localhost:8090/api/v1/performance/ 
curl -i http://localhost:8090/api/v1/performance/0
curl -i http://localhost:8090/api/v1/performance/1 

References: 
https://www.tecmint.com/linux-curl-command-examples/