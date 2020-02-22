# YAHS

**Yet Another HTTP Server** is a stupidly simple http server implementation in Java. 

Its goal is to be as simple to use as possible and to feel a bit like express.

It also is not meant to handle high volumes of traffic and should not be considered production ready.

## Usage

Below is a small example on how to use this software.

### Dependencies

Add the GammelCloud Repo:

```xml
<repositories>
    <repository>
        <id>gammelcloud</id>
        <url>https://nexus.gammel.cloud/repository/maven-public/</url>
    </repository>
</repositories>
```

Add the library to your dependencies:

```xml
<dependency>
    <groupId>sh.lrk</groupId>
    <artifactId>yahs</artifactId>
    <version>1.0.10</version>
</dependency>
```

### Starting a server

Below is an example `main()` but technically you can start the server wherever you need to.

```java
import sh.lrk.yahs.*;

public class Main {
    public static void main(String[] args) {
        Routes routes = new Routes(); // init routes
        routes.add(Method.GET, "/",  ); // add route
        
        Server.start(routes,
        8080, // listen on port 8080
        1_000_000); // max request size: 1M
    }
}
```

### Logging

The easiest way to configure logging is by adding `slf4j-log4j12` to your dependencies and creating a 
`log4j.properties` file in your resources folder containing a configuration similar to the one below:

```
log4j.rootLogger=INFO, STDOUT
log4j.appender.STDOUT=org.apache.log4j.ConsoleAppender
log4j.appender.STDOUT.layout=org.apache.log4j.PatternLayout
log4j.appender.STDOUT.layout.ConversionPattern=%5p [%t] - %m%n
``` 

## Projects using YAHS
- [sh](https://github.com/lfuelling/sh)
- [fail2ban-exporter](https://github.com/lfuelling/fail2ban-exporter)