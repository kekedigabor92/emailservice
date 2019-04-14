# emailservice
Email Service with Spring Boot

[![Build Status](https://travis-ci.org/kekedigabor92/emailservice/builds)](https://travis-ci.org/kekedigabor92/emailservice/builds)
[![HEROKU](https://kekedigabor92-emailservice.herokuapp.com/emailservice/api/swagger-ui.html)](https://kekedigabor92-emailservice.herokuapp.com/emailservice/api/swagger-ui.html)

## Requirements

For building and running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 3](https://maven.apache.org)

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.mycompany.emailservice.EmailServiceApplication` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```

## Testing the application locally

The EmailServiceApplicationIT class file contains integration tests that can be easily run from the IDEA. 

Alternatively you can use the POSTemails.http for manual testing.
It is a nice option as you do not have leave the IDEA to fire a request.
Make sure you start the application first on the proper port.
