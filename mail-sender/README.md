# Mail-sender Guide

## Overview
This application provides for sending the activation code to users by e-mail. This message is needed to confirm the mail when registering or changing it.

## Dependencies
The database-service project uses the following Maven dependencies to provide functionality, database management, caching, logging, and other key aspects of the service:

### Spring boot
1. **Spring Boot Starter** - the dependency that ensures the operation of Spring
2. **Spring boot starter test** - library for working with tests
3. **spring boot starter web** - provides all the necessary tools for web functionality: RESTful controllers, servlets, etc.
4. **Spring cloud config client** - a client that provides configuration for a microservice
5. **Spring boot starter mail** - provides the main function of the application, namely support for sending messages to e-mail

### Other
1. **Domain** is a module containing the main models and auxiliary components for the operation of a microservice.
2. **Lombok** - used to reduce the template code

## Configuration
### 1. Getting the configuration
In the microservice, it is possible to get the configuration from config service, so you can specify the name of the service and the address to which the configuration request will be sent
```yaml
spring:
  application:
    name: [APP_NAME]
  config:
    import: configserver:[CONFIG_SERVER_ADDRESS]
```

### 2. Connection data
```yaml
microservice:
  connection:
    mail-sender:
      protocol: [http/https]
      hostname: [HOST]
      port: [PORT]
      path: /api/service/mail_sender
server:
  port: ${microservice.connection.database.port}
  servlet:
    context-path: ${microservice.connection.database.path}
```

### 3. Sending messages
Settings for working with _Spring boot starter mail_
```yaml
spring:
  mail:
    debug: false
    host: smtp.yandex.ru
    username: [EMAIL_FOR_SENDING]
    password: [PASSWORD]
    port: 465
    protocol: smtps
```

### 4. Generating a link to send an activation code
The main purpose of sending an email is a link with an activation code. The link is copied from these settings:
```yaml
mail:
  sending:
    activation-code:
      confirmation:
        path:
          protocol: ${microservice.connection.gateway.protocol}
          hostname: [HOSTNAME]
          port: ${microservice.connection.gateway.port}
          base-path: ${microservice.connection.gateway.path}
```

## REST methods

### 1. Sending the activation code for registration
### Request example
```http request
POST http://localhost:8095/api/service/mail_sender/active_code/signup
Content-Type: application/json

{
  "sender_username": "Ivan",
  "recipient_email": "IvanIvanov@mail.com",
  "active_code":  "TEST_ACTIVE_CODE"
}
```

### Successful response:
```http request
HTTP/1.1 204 
Date: Fri, 31 May 2024 08:31:30 GMT
Keep-Alive: timeout=60
Connection: keep-alive
```
The method returns the successful status: NO_CONTENT

### 2. Sending an activation code to confirm the email change 
### Request example
```http request
POST http://localhost:8095/api/service/mail_sender/active_code/reset/email
Content-Type: application/json

{
  "sender_username": "Ivan",
  "recipient_email": "IvanIvanov@mail.com",
  "active_code":  "TEST_ACTIVE_CODE"
}
```

### Successful response:
```http request
HTTP/1.1 204 
Date: Fri, 31 May 2024 08:52:06 GMT
Keep-Alive: timeout=60
Connection: keep-alive
```
The method returns the successful status: NO_CONTENT

### Additional links to documentation
For more information, please refer to the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.1.5/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.1.5/maven-plugin/reference/html/#build-image)