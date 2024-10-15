# Spring Boot 2, H2 Database, and Infinispan 14

## What is this
Basically a simple Java code to do insert to database (h2), do some data synchronisation between it and cache (infinispan), and display the result from cache.

## Concept
Pretty much the same as the previous repo (`https://github.com/edwin/spring-boot-2-and-infinispan-15/`), however we are trying to connect to **Infinispan 15** using **Infinispan 14** client.

```
+---------------------+                +-----------------------------+
| insert data to h2   | --- sync ----> |   get data from infinispan  |
+---------------------+                +-----------------------------+
```

## Version
- Infinispan Server 'I'm Still Standing' 15.0.7.Final
- Infinispan Client 14.0.31.Final
- Java 17
- Spring Boot 2.7.8

## Cache Configuration
```json
{
  "replicated-cache": {
    "mode": "SYNC",
    "statistics": true,
    "encoding": {
      "media-type": "application/x-protostream"
    },
    "indexing": {
      "enabled": true,
      "storage": "filesystem",
      "startup-mode": "AUTO",
      "indexing-mode": "AUTO",
      "indexed-entities": [
        "proto.User"
      ]
    }
  }
}
```

## How to Test
Get list of Users from Cache
```
$ curl -kv http://localhost:8080/user
*   Trying [::1]:8080...
* Connected to localhost (::1) port 8080
> GET /user HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/8.4.0
> Accept: */*
>
< HTTP/1.1 200
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Tue, 15 Oct 2024 12:53:39 GMT
<
* Connection #0 to host localhost left intact
[]                                           
```

Insert into Database
```
$ curl -kv http://localhost:8080/user -X POST \ 
        -d '{"name":"Ryoko Hirosue", "age":29,"address":"Ciledug"}' \
         -H "Content-Type: application/json"
*   Trying [::1]:8080...
* Connected to localhost (::1) port 8080
> POST /user HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/8.4.0
> Accept: */*
> Content-Type: application/json
> Content-Length: 54
>
< HTTP/1.1 201
< Content-Length: 0
< Date: Tue, 15 Oct 2024 12:55:17 GMT
<
* Connection #0 to host localhost left intact
```

Sync the data
```
$ curl -kv http://localhost:8080/sync
*   Trying [::1]:8080...
* Connected to localhost (::1) port 8080
> GET /sync HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/8.4.0
> Accept: */*
>
< HTTP/1.1 201
< Content-Length: 0
< Date: Tue, 15 Oct 2024 12:56:08 GMT
<
* Connection #0 to host localhost left intact
```

Get list of Users from Cache
```
$ curl -kv http://localhost:8080/user
*   Trying [::1]:8080...
* Connected to localhost (::1) port 8080
> GET /user HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/8.4.0
> Accept: */*
>
< HTTP/1.1 200
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Tue, 15 Oct 2024 12:56:45 GMT
<
* Connection #0 to host localhost left intact
[{"name":"Ryoko Hirosue","age":29,"address":"Ciledug"}]   
```