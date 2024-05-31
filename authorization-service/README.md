# Authorization-service API Guide
Open API v1.0.0

## Overview:
This microservice provides a REST API for user registration, authorization and authentication. Registration takes place with email confirmation

## Content
1. [Dependencies](#dependencies)
2. [Configuration](#configuration)
3. [REST methods](#rest-methods)
    - [Registration](#1-registration)
    - [Email confirmation](#2-email-confirmation-)
    - [Login and password authorization](#3-login-and-password-authorization)
    - [Authorization by refresh token](#4-authorization-by-refresh-token)
    - [Log out of the system](#4-log-out-of-the-system)
4. [Documentation](#additional-links-to-documentation)

## Dependencies:
The database-service project uses the following Maven dependencies to provide functionality, authorization, caching, logging, and other key aspects of the service:

### Spring boot:
1. **Spring Boot Starter** - the dependency that ensures the operation of Spring
2. **Spring boot starter test** - library for working with tests
3. **spring boot starter web** - provides all the necessary tools for web functionality: RESTful controllers, servlets, etc.
4. **spring Web Flux** is a framework used for asynchronous and reactive processing of web requests
5. **Spring Cache** - provides an interface for caching auxiliary data during authorization
6. **Spring Data Redis** - NoSQL storage for the application cache
7. **Spring cloud config client** - a client that provides configuration for a microservice

### Authorization:
1. **Spring security crypto** is a module of the Spring Security library that provides functionality for cryptographic operations. The application is used to encrypt and verify user passwords. This module can be used independently of other Spring Security components.
2. **jjwt api** and **jjwt-impl** are libraries for working with the JSON Web Token, on which authorization in the microservice is based
3. **jjwt-jackson** and **gson** are libraries that provide functionality for working with JSON

### Logging:
1. **Logstash Logback Encoder** is a library for logging data in a specific format
2. **Logger** is a module that implements the work of a custom logger. This logger adds the state of the object to the messages

### Other:
1. **Lombok** - used to reduce the template code
2. **MapStruct** is a framework for mapping different models and entities, reducing the amount of code and potential errors when converting data

### Auxiliary modules:
1. **Domain** is a module containing the main models and auxiliary components for the operation of a microservice.
2. **Layer connector** is a module that provides automatic mapping of models between different layers of the application

## Configuration:
###1. Getting the configuration:
In the microservice, it is possible to get the configuration from config service, so you can specify the name of the service and the address to which the configuration request will be sent
```yaml
spring:
  application:
    name: [APP_NAME]
  config:
    import: configserver:[CONFIG_SERVER_ADDRESS]
```

### 2. Connecting the cache:
The microservice uses redis as a cache storage, data for connection:
```yaml
spring:
  data:
    redis:
      host: [HOST]
      port: [PORT]
```

### 3. Logging:
Since the project uses a custom logger: ObjectStateLogger, which logs not only the message, but also the state of the specified object, it is important to specify the factory for generating the logger. As well as the names of those fields that the logger should mask when logging the state of the object
```yaml
org:
  slf4j:
    LoggerFactory: com.niksob.logger.object_state.factory.ObjectStateLoggerFactory
logger:
  message:
    masked:
      field-names:
        - password
        - rowPassword
        - accessToken
        - access
        - refreshToken
        - refresh
```

### 4. Connection data:
```yaml
microservice:
  connection:
    authorization:
      protocol: [http/https]
      hostname: [HOST]
      port: [PORT]
      path: /api/service/auth
server:
  port: ${microservice.connection.authorization.port}
  servlet:
    context-path: ${microservice.connection.authorization.path}
```

### 5. Setting up Authorization Tokens:
```yaml
auth:
  token:
    expiration-in-minutes:
      access: 5
      refresh: 21600
    secret-key:
      access: "SECRET"
      refresh: "SECRET"
```

1. **auth.token.expiration-in-minutes.access** - indicates what lifetime in minutes the access token will have.
2. **auth.token.expiration-in-minutes.refresh** - indicates what lifetime in minutes the refresh token will have.
3. **auth.token.secret-key** - settings containing secret keys for decrypting tokens

_Access token_ is a token that the user sends along with each request. Thanks to this, the service understands who is contacting it. And based on this information, it decides whether to provide access to the data or not.

_Refresh token_ is a token that the user sends to receive a new pair of access-refresh tokens. Thanks to this, the service understands who wants to receive the data. And based on this information, it decides whether to issue a new pair or not.

## Basic Entities
### 1. AuthToken
The main model of the microservice, which contains **_access_** and **_refresh_** tokens. And also **_device_** - a device linked to a pair of tokens.
```java
public class AuthToken {
    private final AuthTokenId id;
    private final UserId userId;
    private final AccessToken access;
    private final RefreshToken refresh;
    private String device;
}
```

### 2. RowLoginInDetails
The model that the application receives to perform authorization
```java
public class RowLoginInDetails {
    private final Username username;
    private final RowPassword rowPassword;
    private final String device;
}
```

### 3. SignupDetails
A model for transmitting data during registration
```java
public class SignupDetails {
    private final Email email;
    private final Username username;
    private final RowPassword rowPassword;
}
```

### 4. SignOutDetails
A model with logout data  
```java
public class SignOutDetails {
    private final UserId userId;
    private final String device;
}
```

## REST methods
The microservice deals exclusively with user registration, authorization and authentication. And does not deal with the security of the entire application. There is a gateway service for these purposes. The service sends and receives data only in JSON format.

### 1. Registration
### Request example
```http request
POST http://80.242.58.161:8092/api/service/auth/signup
Content-Type: application/json

{
  "email": "IvanIvanov@mail.com",
  "username": "Ivan",
  "password": "SECRET"
}
```

### Successful response:
```http request
HTTP/1.1 201
Content-Length: 0
Date: Wed, 22 May 2024 15:28:13 GMT
Keep-Alive: timeout=60
Connection: keep-alive
```

### Errors
The email is specified in an incorrect format:
```http request
HTTP/1.1 400
Content-Type: application/json
Transfer-Encoding: chunked
Date: Wed, 22 May 2024 15:29:38 GMT
Connection: close

{
  "timestamp": "2024-05-22T15:29:38.723125376",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid email",
  "path": "/api/service/auth"
}
```

### 2. Email Confirmation 
After sending the registration data, a link with an activation code is sent to the mail

### Example
GET https://80.242.58.161:8082/api/auth/signup/activate/2e288b77-2705-4719-9478-f99cc9bce21d

After clicking on this link, the microservice creates a user object and stores it in the database.

### 3. Login and password authorization
### Example
The authorization process begins with receiving a pair of access-refresh tokens  

GET http://localhost:8092/api/service/auth/signout?userId=13&device=MY_DEVICE
### Successful response
```http request
HTTP/1.1 201
Content-Type: application/json
Transfer-Encoding: chunked
Date: Thu, 23 May 2024 09:29:55 GMT
Keep-Alive: timeout=60
Connection: keep-alive

{
  "id": "6",
  "device": "MY_DEVICE",
  "user_id": "13",
  "access_token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJJdmFuIiwiZXhwIjoxNzE2NDU2ODk1LCJkZXYiOiJNWV9ERVZJQ0UiLCJ1c2VySWQiOiIxMyJ9.VTQVgZpodrOS7021R4KEHY4_T6c7kyJAfbg0d8TmDHYGW5LHOUnaHd4YpQbXdC0kEkSOARhyPPj_4adE2FSBkA",
  "refresh_token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJJdmFuIiwiZXhwIjoxNzE3NzUyNTk1LCJkZXYiOiJNWV9ERVZJQ0UiLCJ1c2VySWQiOiIxMyJ9.LsVS7NUdRV2frzfJHNcp8Vm2886MAugPiUiiG3il72oL1oUtiXOxBg1taucWC2axJjhR45jYFqeHh0cKY_S4bg"
}
```
After receiving, a pair of tokens is stored in the database. 
The Access token must be specified in the header of each request as a Bearer to go through the authorization and authentication process when interacting with data.

### Errors
If you enter an incorrect password:
```http request
HTTP/1.1 403
Content-Type: application/json
Transfer-Encoding: chunked
Date: Thu, 23 May 2024 09:35:10 GMT
Keep-Alive: timeout=60
Connection: keep-alive

{
  "timestamp": "2024-05-23T09:35:10.825692612",
  "status": 403,
  "error": "Forbidden",
  "message": "Wrong password",
  "path": "/api/service/auth"
}
```

When specifying an invalid username:
```http request
HTTP/1.1 403
Content-Type: application/json
Transfer-Encoding: chunked
Date: Thu, 23 May 2024 09:35:55 GMT
Keep-Alive: timeout=60
Connection: keep-alive

{
  "timestamp": "2024-05-23T09:35:55.809890768",
  "status": 403,
  "error": "Forbidden",
  "message": "User not registered",
  "path": "/api/service/auth"
}
```

### 4. Authorization by refresh token
```http request
POST http://80.242.58.161:8092/api/service/auth/token/refresh
Content-Type: application/json

{
  "refresh_token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJJdmFuIiwiZXhwIjoxNzE3NzUyNTk1LCJkZXYiOiJNWV9ERVZJQ0UiLCJ1c2VySWQiOiIxMyJ9.LsVS7NUdRV2frzfJHNcp8Vm2886MAugPiUiiG3il72oL1oUtiXOxBg1taucWC2axJjhR45jYFqeHh0cKY_S4bg"
}
```

### Successful response
```http request
HTTP/1.1 201
Content-Type: application/json
Transfer-Encoding: chunked
Date: Thu, 23 May 2024 09:30:41 GMT
Keep-Alive: timeout=60
Connection: keep-alive

{
  "id": "6",
  "device": "MY_DEVICE",
  "user_id": "13",
  "access_token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJJdmFuIiwiZXhwIjoxNzE2NDU2OTQxLCJkZXYiOiJNWV9ERVZJQ0UiLCJ1c2VySWQiOiIxMyJ9.sFweEDeZfjmQazGaRtdc-2Urd-Ml88UAu5uPfrDOow9_j2Hh3YT9ah3-7LMbqcebo1PB4ZK1uWXafcv5XG9gIA",
  "refresh_token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJJdmFuIiwiZXhwIjoxNzE3NzUyNjQxLCJkZXYiOiJNWV9ERVZJQ0UiLCJ1c2VySWQiOiIxMyJ9.Jfc3u0cz1_aNT-po6CnZtylzOGlL04cEGNTGm0oRk74f4o-EF4suv3VLSit4VYDt3SUhQSyNckmr4kY6at_JRw"
}
```

### Errors
When specifying an invalid refresh token:
```http request
HTTP/1.1 403
Content-Type: application/json
Transfer-Encoding: chunked
Date: Thu, 23 May 2024 09:37:12 GMT
Keep-Alive: timeout=60
Connection: keep-alive

{
  "timestamp": "2024-05-23T09:37:12.339424640",
  "status": 403,
  "error": "Forbidden",
  "message": "Invalid token",
  "path": "/api/service/auth"
}
```

### 4. Log out of the system
This method removes a pair of access-refresh tokens for a specific device from the database. After that, they become invalid 
### Example
GET http://80.242.58.161:8092/api/service/auth/signout?userId=13&device=MY_DEVICE

**_parameters:_**
- userId - user ID
- device - name of the device 

### Successful response
The response returns a successful status: NO_CONTENT
```http request
HTTP/1.1 204
Date: Thu, 23 May 2024 09:41:07 GMT
Keep-Alive: timeout=60
Connection: keep-alive
```

### Errors
Non-existent _user id_ or _device_:
```http request
HTTP/1.1 404
Content-Type: application/json
Transfer-Encoding: chunked
Date: Thu, 23 May 2024 09:41:50 GMT
Keep-Alive: timeout=60
Connection: keep-alive

{
  "timestamp": "2024-05-23T09:41:50.229626948",
  "status": 404,
  "error": "Not Found",
  "message": "Resource not found",
  "path": "/api/service/auth"
}
```

### 5. Log out from all devices
This method removes access-refresh token pairs for all devices from the database. After that, they become invalid

### Example
GET http://80.242.58.161:8092/api/service/auth/signout/all?userId=13
### Successful response
```http request
HTTP/1.1 204
Date: Thu, 23 May 2024 09:47:07 GMT
Keep-Alive: timeout=60
Connection: keep-alive
```
This method returns a successful response even if the user does not exist

### Additional links to documentation
For more information, please refer to the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.1.5/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.1.5/maven-plugin/reference/html/#build-image)