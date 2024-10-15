# Spring Boot 2, H2 Database, and Infinispan 14

## What is this
Basically a simple Java code to do insert to database (h2), do some data synchronisation between it and cache (infinispan), and display the result from cache.

## Concept
Pretty much the same as the previous repo (https://github.com/edwin/spring-boot-2-and-infinispan-15/), however we are trying to connect to Infinispan 15 using Infinispan 14 client.

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