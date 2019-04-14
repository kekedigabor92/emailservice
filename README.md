# emailservice
Email Service with Spring Boot

[![Build Status](https://travis-ci.org/kekedigabor92/emailservice.svg?branch=dev)](https://travis-ci.org/kekedigabor92/emailservice/builds)
[![HEROKU](https://github.com/heroku/favicon/raw/master/favicon.iconset/icon_32x32.png)](https://kekedigabor92-emailservice.herokuapp.com/emailservice/api/swagger-ui.html)

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

The EmailServiceApplicationIT class file contains integration tests that can be easily run from the IDEA or using command line.

```shell
mvn integration-test
``` 

Alternatively you can use the POSTemails.http for manual testing.
It is a nice option as you do not have leave the IDEA to fire a request.
Make sure you start the application first on the proper port.

## Details of the submission, design and implementation decisions

The Java 8 and Spring Boot combination was basically expected in the task, but I would have chosen these anyways.

I decided to use groovy for testing purposes as I find it very concise and easy to read.
In addition to that, the possibility and especially the way of creating parameterized tests is 
overwhelmingly impressive for me. To keep the build time short, most unit tests do not start up the the spring context
but rather only utilize mocked instances. There is one test class though, responsible for checking if the spring app could start.
I like to have this class as it reveals possible dependency injection problems in build time and we can avoid these
issues during startup. Almost every time I use constructor injection since in my opinion it is easier to read,
we can see if we start having too many arguments for a class as opposed to setter-based injection.
Plus, @Autowired can be omitted for classes with one constructor. 

I provided a simple swagger documentation, however it is not sufficient for testing purposes.

I picked maven because I feel more confident with this build tool, but gradle could have been sufficient too.

I tried to keep classes, methods small and short and easy to test. The application is splitted into layers.
The controller layer is responsible for handling the incoming requests and delegating them further to the service layer.
The service layer contains the business logic basically and decides how the request should be handled.
As we lack database, no repository layer is needed. 

The standard javax.validation is used in order to validate the format of the request body. 
As a result the validation in the service layer can be much simpler, we can ensure for instance that mandatory 
fields will not be null and the populated fields will in proper format. 

Additional validations needed to be applied in order for our API to be compliant with SendGrid.
These include for example total recipient limit, e-mail address uniqueness. 

Based on the SendGrid's error page, I implemented all validations (hopefully) for the currently supported functionality to prevent requests resulting in 400 error.
https://sendgrid.com/docs/API_Reference/Web_API_v3/Mail/errors.html

As the service needs to deal with multiple clients simultaneously, the logging output needs to be differentiated from one client to another.
Here comes MDC in the picture. A UUID is associated to each request along with the request url and these can be easily added to the logging configuration. 

For CI/CD, Travis CI seemed to be the simplest and easiest way to go. It supports deployment to Heroku, which is free cloud provider.
Opting for docker was not a question, as it is the leading container platform.

Regarding version control and branching, please note that I pushed my changes directly to dev. 
In my opinion it would have been overkill to create feature branches and PR-s just for myself, so I just kept it simple.

Thanks again for the opportunity. :)  