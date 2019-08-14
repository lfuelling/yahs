# YAHS

**Yet Another HTTP Server** is a stupidly simple http server implementation in Java. 

Its goal is to be as simple to use as possible and to feel a bit like express.

It also is not meant to handle high volumes of traffic and should not be considered production ready.

## Usage

Add the GammelCloud Repo:

```xml
<repositories>
    <repository>
        <id>gammelcloud</id>
        <url>https://nexus.gammel.cloud/repository/maven-public/</url>
    </repository>
</repositories>
```

Add it to your dependencies:

```xml
<dependency>
    <groupId>sh.lrk</groupId>
    <artifactId>yahs</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

Below is an example `main()` but technically you can start the server wherever you need to.

```java
import sh.lrk.yahst.Method;
import sh.lrk.yahst.Response;
import sh.lrk.yahst.Routes;
import sh.lrk.yahst.Server;

public class Main {
    public static void main(String[] args) {
        Routes routes = new Routes(); // init routes
        routes.add(Method.GET, "/", req -> new Response("Hello World!", Response.Status.OK)); // add route
        
        Server.start(routes,
        8080, // listen on port 8080
        1_000_000); // max request size: 1M
    }
}
```
