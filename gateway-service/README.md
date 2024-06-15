# API gateway-service Guide
Open API v1.0.0

## Overview
gateway-service is the entry point for the entire application. This microservice is responsible for the security and functionality management of other microservices.

## Content
1. [Dependencies](#dependencies)
2. [Configuration](#configuration)
3. [REST methods](#rest-methods)
    - [User Account Management](#user-account-management)
        - [Registration](#1-registration)
        - [Email confirmation after registration](#2-email-confirmation-after-registration)
        - [Login and password authorization](#3-login-and-password-authorization)
        - [Authorization by refresh token](#4-authorization-by-refresh-token)
        - [Password Reset](#5-password-reset)
        - [Changing the mail](#6-changing-the-mail)
        - [Confirmation of the mail after its change](#7-confirmation-of-the-mail-after-its-change)
        - [Logout](#8-logout)
    - [User Data Management Methods](#2-user-data-management-methods)
        - [User](#user)
        - [Mood tag](#mood-tag)
        - [Mood entry](#mood-entry)
4. [Additional links to documentation](#additional-links-to-documentation)


## Dependencies
The gateway-service project uses the following Maven dependencies to provide functionality:

### Spring boot
1. **Spring Boot Starter** - the dependency that ensures the operation of Spring
2. **Spring boot starter test** - library for working with tests
3. **spring Web Flux** is a framework used for asynchronous and reactive processing of web requests

### Security
1. **Spring boot starter security** is a module that provides a security system in the application: protects routes and thereby controls access to various microservices.

### Logging
1. **Logstash Logback Encoder** is a library for logging data in a specific format
2. **Logger** is a module that implements the work of a custom logger. This logger adds the state of the object to the messages

### Other
1. **Lombok** - used to reduce the template code
2. **MapStruct** is a framework for mapping different models and entities, reducing the amount of code and potential errors when converting data

### Auxiliary modules
1. **Domain** is a module containing the main models and auxiliary components for the operation of a microservice.
2. **Layer connector** is a module that provides automatic mapping of models between different layers of the application

## Configuration
###1. Getting the configuration
In the microservice, it is possible to get the configuration from config service, so you can specify the name of the service and the address to which the configuration request will be sent
```yaml
spring:
  application:
    name: [APP_NAME]
  config:
    import: configserver:[CONFIG_SERVER_ADDRESS]
```

### 2. Logging
Since the project uses a custom logger: ObjectStateLogger, which logs not only the message, but also the state of the object, it is important to specify the factory for generating the logger. As well as the names of those fields that the logger must mask when logging the state of the object
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

### 3. Connection data
```yaml
microservice:
  connection:
    database:
      protocol: [http/https]
      hostname: [HOST]
      port: [PORT]
      path: /api/service/gateway
server:
  port: ${microservice.connection.gateway.port}
  servlet:
    context-path: ${microservice.connection.gateway.path}
```

### Configuring Spring secure security
```java
@Configuration
@AllArgsConstructor
@EnableWebFluxSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    private final ReactiveAuthenticationManager authenticationManager;
    @Qualifier("accessTokenFilter")
    private final AuthenticationWebFilter accessTokenFilter;
    private final SecurityContextRepository securityContextRepository;
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(
                                AuthControllerPaths.BASE_URI + AuthControllerPaths.SIGNUP + "/**",
                                AuthControllerPaths.BASE_URI + AuthControllerPaths.EMAIL_RESETTING_ACTIVATION + "/**",
                                AuthTokenControllerPaths.BASE_URI + "/**"
                        ).permitAll()
                        .anyExchange().authenticated())
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository)
                .addFilterAt(accessTokenFilter, SecurityWebFiltersOrder.AUTHENTICATION);
        return http.build();
    }
}
```

### Explanations
1. CSRF protection is disabled. It is not needed in the application, since authorization occurs by transferring bearer in the form of an access token with each request
2. The login form will be disabled, since the application implements the REST API, the server is not configured to output html pages
3. Access to all resources is prohibited, except for registration and authorization services
4. Basic http authentication is disabled, since the authentication function is implemented by bearer, which comes with each client request
5. An authorization manager is added, which performs authorization using access token
6. A security context is added to save the authorized user after receiving the bearer 
7. The AccessTokenFilter filter is added, which determines which resources the user has access to based on bearer


## REST methods
The microservice is the entry point to the application and is responsible for security by filtering incoming traffic

## User Account Management
### 1. Registration
### Request example
```http request
POST https://moodtracker.ru/api/auth/signup
Content-Type: application/json

{
  "email": "IvanIvanov@mail.com",
  "username": "Ivan",
  "password": "Very_secret0"
}
```
Password requirements: at least one letter in upper case, at least one digit. The password is at least 8 characters long

### Successful response
```http request
HTTP/1.1 201 Created
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer
content-length: 0
```

### Errors
The email is specified in an incorrect format:
```http request
HTTP/1.1 400 Bad Request
Content-Type: application/json
Content-Length: 133
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer

{
  "timestamp": "2024-05-24T12:42:01.992453454",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid email",
  "path": "/api/auth"
}
```

The password is specified in an incorrect format:
```http request
HTTP/1.1 403 
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer
Content-Type: application/json
Content-Length: 131
Date: Tue, 28 May 2024 10:44:47 GMT
Keep-Alive: timeout=60
Connection: keep-alive

{
  "timestamp": "2024-05-28T13:44:47.157106",
  "status": 403,
  "error": "Forbidden",
  "message": "Invalid password",
  "path": "/api/auth"
}
```

Registration has already been made by the specified email or username:
```http request
HTTP/1.1 400
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer
Content-Type: application/json
Content-Length: 166
Date: Tue, 28 May 2024 11:25:25 GMT
Connection: close

{
  "timestamp": "2024-05-28T14:25:25.743502",
  "status": 400,
  "error": "Bad Request",
  "message": "User already registered by this email or username",
  "path": "/api/auth"
}
```

### 2. Email confirmation after registration
In order to complete the registration process, you need to go to the email and click on the link sent with the activation code

### Request example
GET https://moodtracker.ru/api/auth/signup/activate/0da79d34-f9f3-48a4-bee2-9a56019fabea

### Successful response
```http request
HTTP/1.1 200 OK
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer
content-length: 0
```

### Errors
Clicking on the link again:
```http request
HTTP/1.1 409 Conflict
Content-Type: application/json
Content-Length: 141
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer

{
  "timestamp": "2024-05-24T14:22:06.130924825",
  "status": 409,
  "error": "Conflict",
  "message": "Duplicate signup attempt",
  "path": "/api/auth"
}
```

Invalid activation code is specified in the link:
```http request
HTTP/1.1 409 Conflict
Content-Type: application/json
Content-Length: 147
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer

{
  "timestamp": "2024-05-24T14:22:59.809833205",
  "status": 409,
  "error": "Conflict",
  "message": "Invalid signup activation code",
  "path": "/api/auth"
}
```

### 3. Login and password authorization
### Example
The authorization process begins with receiving a pair of access-refresh tokens

```http request
POST https://moodtracker.ru/api/auth/token
Content-Type: application/json

{
  "username": "Ivan",
  "password": "Very_secret0",
  "device": "MY_DEVICE"
}
```

### Successful response
```http request
HTTP/1.1 201 Created
Content-Type: application/json
Content-Length: 467
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer

{
  "id": "9",
  "device": "MY_DEVICE",
  "user_id": "14",
  "access_token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJJdmFuIiwiZXhwIjoxNzE2NTYxMDk2LCJkZXYiOiJNWV9ERVZJQ0UiLCJ1c2VySWQiOiIxNCJ9.bE1iF2xZQSlIF5grsLKiqqoNijqX8hAMiww7sUmxWvAdx0yLfcPh9FmiImOys8A-ZGpHzj4a9moTqN8eF-Sptg",
  "refresh_token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJJdmFuIiwiZXhwIjoxNzE3ODU2Nzk2LCJkZXYiOiJNWV9ERVZJQ0UiLCJ1c2VySWQiOiIxNCJ9.uWGiQ2cq0U24wJkgLewrJMRXdq4XvkQvkAMulvPz-fbmKnV9kROeBTahPwiGRJtQWOBJ71YzblrRWgvtDQtSWQ"
}
```

### Errors
Invalid username:
```http request
HTTP/1.1 403 Forbidden
Content-Type: application/json
Content-Length: 137
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer

{
  "timestamp": "2024-05-24T14:27:19.022578456",
  "status": 403,
  "error": "Forbidden",
  "message": "User not registered",
  "path": "/api/auth"
}
```

Invalid password:
```http request
HTTP/1.1 403 Forbidden
Content-Type: application/json
Content-Length: 132
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer

{
  "timestamp": "2024-05-24T14:28:43.143209691",
  "status": 403,
  "error": "Forbidden",
  "message": "Wrong password",
  "path": "/api/auth"
}
```

### 4. Authorization by refresh token
### Example
```http request
POST https://moodtracker.ru/api/auth/token/refresh
Content-Type: application/json

{
  "refresh_token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJuaWtpdG9zMDAyM0BnbWFpbC5jb20iLCJleHAiOjE3MTQ5OTQ2NjYsInVzZXJJZCI6IjQiLCJkZXYiOiJURVNUX0RFVklDRSJ9.ZY0feVssnWkPTJFIhyrXuyr6M0Oit9Zl2FOLwAHExFDVF5Rqp63CU6tWQW3yNRfng4hjkaLAX3C0OaUT3i8YKQ"
}
```

### Successful response
```http request
HTTP/1.1 201 Created
Content-Type: application/json
Content-Length: 467
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer

{
  "id": "9",
  "device": "MY_DEVICE",
  "user_id": "14",
  "access_token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJJdmFuIiwiZXhwIjoxNzE2NTYxMzg2LCJkZXYiOiJNWV9ERVZJQ0UiLCJ1c2VySWQiOiIxNCJ9.XwnF7fNoHDXz2kgI4akMtMaoEnCRLspsaIEZjup_Sk1oaya5mns2FaKz2co537dHiLOYiQtlSgnEO8HjxBSeuQ",
  "refresh_token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJJdmFuIiwiZXhwIjoxNzE3ODU3MDg2LCJkZXYiOiJNWV9ERVZJQ0UiLCJ1c2VySWQiOiIxNCJ9.KDH9FXqpTkYzSajKx-z9BgxBtIlVOTSRM4RNcomiAUkJFrmUlZv4u-wNCzYVJzDDWPUyJ2g3obA6B92fLbVnSg"
}
```

### Errors
Outdated or incorrect refresh token:
```http request
Content-Type: application/json
Content-Length: 131
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer

{
  "timestamp": "2024-05-24T14:31:40.590979711",
  "status": 403,
  "error": "Forbidden",
  "message": "Invalid token",
  "path": "/api/auth"
}
```
After receiving the access token, you can make other requests to the service by specifying the token as a Bearer in the authorization header.

**If the token in the request is non-specific or outdated:**
```http request
HTTP/1.1 403 Forbidden
Content-Type: application/json
Content-Length: 127
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer

{
  "timestamp": "2024-05-24T14:37:25.947+00:00",
  "path": "/auth/signout",
  "status": 403,
  "error": "Forbidden",
  "requestId": "4173e860-19"
}
```

### 5. Password Reset
You can change the password only by sending a request with the old password for verification and the new password for change

### Example
```http request
HTTP/1.1 204 No Content
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer
```
The method returns the successful status: NO_CONTENT

### Errors
The old password does not match the encrypted password in the system:
```http request
HTTP/1.1 403 Forbidden
Content-Type: application/json
Content-Length: 125
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer

{
  "timestamp": "2024-05-30T11:20:19.030995",
  "status": 403,
  "error": "Forbidden",
  "message": "Incorrect password",
  "path": "/api/auth"
}
```

The new password does not meet the security requirements:
```http request
HTTP/1.1 403 Forbidden
Content-Type: application/json
Content-Length: 159
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer

{
  "timestamp": "2024-05-30T11:20:50.023213",
  "status": 403,
  "error": "Forbidden",
  "message": "The password does not meet the security requirements",
  "path": "/api/auth"
}
```

### 6. Changing the mail
You can change your email only after confirming a new one

### Example
Request for confirmation of mail:
```http request
POST https://moodtracker.ru/api/auth/reset/email
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJJdmFuIiwiZXhwIjoxNzE3MDU4NDk1LCJkZXYiOiJNWV9ERVZJQ0UiLCJ1c2VySWQiOiIxIn0.OycPsjgFgHqSyXWKBk131jb1CH5jqrujzimJpR0krWBNEI5j0AMlwxV1XhcPyH2NYsCYeA0RbfUQ1FBaLsfMbQ
Content-Type: application/json

{
  "email": "NewAndFreshIvanov@mail.com"
}
```

### Successful response
```http request
HTTP/1.1 204 No Content
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer
```

### 7. Confirmation of the mail after its change
### Example
GET https://moodtracker.ru/api/auth/reset/email/activate/35225058-e09b-4923-b0a8-de68514578b8

### Successful response
```http request
HTTP/1.1 204 No Content
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer
```
The method returns the successful status: NO_CONTENT

### Errors
The activation code has already been applied, but is being sent by the user again:
```http request
HTTP/1.1 409 Conflict
Content-Type: application/json
Content-Length: 127
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer

{
  "timestamp": "2024-05-30T12:12:09.783452",
  "status": 409,
  "error": "Conflict",
  "message": "Wrong activation code",
  "path": "/api/auth"
}
```

### 8. Logout
This method removes a pair of access-refresh tokens for a specific device from the system. After that, they become invalid

### Example
```http request
GET https://moodtracker.ru/api/auth/signout?device=MY_DEVICE
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJJdmFuIiwiZXhwIjoxNzE2NTYxNjgxLCJkZXYiOiJNWV9ERVZJQ0UiLCJ1c2VySWQiOiIxNCJ9.iX2qWbsnDlwHXTS3xHlo0UMV8VlHTScBYvYkgmfsCJfnfNzHPl5TADwt94ert8sa9-603GSSXPmNdNDv9sb8Ig
```

### Successful response
```http request
HTTP/1.1 204 No Content
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer
```

### Errors
```http request
HTTP/1.1 403 Forbidden
Content-Type: application/json
Content-Length: 127
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer

{
  "timestamp": "2024-05-30T07:25:37.952+00:00",
  "path": "/auth/signout",
  "status": 403,
  "error": "Forbidden",
  "requestId": "e5ba9907-10"
}
```

To delete all access-refresh token pairs:
```http request
GET https://moodtracker.ru:8092/api/auth/signout/all
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJJdmFuIiwiZXhwIjoxNzE3MDU0NzY3LCJ1c2VySWQiOiIxIiwiZGV2IjoiTVlfREVWSUNFIn0.xwWPneNu9U7dO7IPDhYzAGmhArIYSKUQ-EjX8PEIx_vhl3yRuvFSk3SZomTMEk4Dn70sNrbJZEc_2WWx9BsdWA
```

### Successful response
The response returns the successful status: NO_CONTENT
```http request
HTTP/1.1 204 No Content
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer
```

### Errors
The method can return an error only in case of repeated logout due to the fact that the token has become invalid:
```http request
HTTP/1.1 403 Forbidden
Content-Type: application/json
Content-Length: 130
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer

{
  "timestamp": "2024-05-30T07:18:06.400+00:00",
  "path": "/auth/signout/all",
  "status": 403,
  "error": "Forbidden",
  "requestId": "36fd3f93-9"
}
```

## 2. User data management methods
## User

## 1. Getting a user
### Example
```http request
GET https://moodtracker.ru/api/user
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJJdmFuIiwiZXhwIjoxNzE3MDU1OTY4LCJ1c2VySWQiOiIxIiwiZGV2IjoiTVlfREVWSUNFIn0.nCFLz-Z1V9OdkiVHUcKW81q4UsW8jmA1CHme9nnM6A3zG7G0gEUuiuoaKPvTgzqx-l7c62EmK_FIfGmnQNwtAA
```

### Successful response
```http request
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 130
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer

{
  "id": 1,
  "email": "IvanIvanov@mail.com",
  "username": "Ivan",
  "password": "$2a$08$8dy2NOv/bO8XHXAhqxRvo.qwGKOhxn2U78x94S.3zHLTsx.4qdRcq"
}
```

## 2. Getting complete user data
The method returns the user along with the mood states and tags

### Example
```http request
GET https://moodtracker.ru/api/user/full
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJJdmFuIiwiZXhwIjoxNzE3MDU1OTY4LCJ1c2VySWQiOiIxIiwiZGV2IjoiTVlfREVWSUNFIn0.nCFLz-Z1V9OdkiVHUcKW81q4UsW8jmA1CHme9nnM6A3zG7G0gEUuiuoaKPvTgzqx-l7c62EmK_FIfGmnQNwtAA
```

### Successful response
```http request
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 343
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer

{
  "id": 1,
  "email": "IvanIvanov@mail.com",
  "username": "Ivan",
  "password": "$2a$08$8dy2NOv/bO8XHXAhqxRvo.qwGKOhxn2U78x94S.3zHLTsx.4qdRcq",
  "mood_entries": [
    {
      "id": 1,
      "degree": 1,
      "user_id": 1,
      "date_time": "2024-05-28T11:23:51",
      "mood_tag_ids": [
        "1",
        "2"
      ]
    }
  ],
  "mood_tags": [
    {
      "id": 2,
      "name": "sleep",
      "degree": 0,
      "userId": 1
    },
    {
      "id": 1,
      "name": "girl friend",
      "degree": 3,
      "userId": 1
    }
  ]
}
```
## 2. Saving a new user
Since saving a new user is related to the security system, saving is performed [with its help] (#registration-authentication-and-authorization-)

## 3. Changing the user
Using this method, all user fields except email and password are subject to change. To reset the password, use the [special method](#5-reset-password). The same as for [mail change] (#6-mail change).
The data of mood states and tags must also be changed using third-party methods.

### Example
Changing the _username_:
```http request
PUT https://moodtracker.ru/api/user
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJJdmFuIiwiZXhwIjoxNzE3MDYzNDc1LCJkZXYiOiJNWV9ERVZJQ0UiLCJ1c2VySWQiOiIxIn0.L0KHWJ2cepwUvDFbiZfX0-Z9vDD-Lo7PTqWDTpXhwGohsjr4_guZ1y99g-IgCOtilfEjtg4N_SImenQnDaqoUQ

{
  "username": "Ivan"
}
```

### Successful response
```http request
HTTP/1.1 204 No Content
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer
```

### Errors
The user sent incorrect data for modification. For example, username already exists:
```http request
HTTP/1.1 400 Bad Request
Content-Type: application/json
Content-Length: 149
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer

{
  "timestamp": "2024-05-30T12:42:34.001212",
  "status": 400,
  "error": "Bad Request",
  "message": "User info was not update in the database",
  "path": "/api/user"
}
```

## 4. Deleting a user
### Example
```http request
DELETE https://moodtracker.ru/api/user
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJJdmFuIiwiZXhwIjoxNzE3MDYzNDc1LCJkZXYiOiJNWV9ERVZJQ0UiLCJ1c2VySWQiOiIxIn0.L0KHWJ2cepwUvDFbiZfX0-Z9vDD-Lo7PTqWDTpXhwGohsjr4_guZ1y99g-IgCOtilfEjtg4N_SImenQnDaqoUQ
```

### Successful response
```http request
HTTP/1.1 204 No Content
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer
```

## Mood tag
## 1. Getting a mood tag

### Example
```http request
GET https://moodtracker.ru/api/mood_tag
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJJdmFuIiwiZXhwIjoxNzE3MDY1MzUzLCJkZXYiOiJNWV9ERVZJQ0UiLCJ1c2VySWQiOiI0In0.93im0syhNWJ8OOxUasa5MWqjfiTnIMkQkBnVfGV8CJJa87JSI18ZniWUasuZQp64HqAM6aj8KBgseT4TBjdpXw
```

### Successful response
The response contains a list of all the user's mood tags:
```http request
HTTP/1.1 200 OK
transfer-encoding: chunked
Content-Type: application/json
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer

[
  {
    "id": 1,
    "name": "girl friend",
    "degree": 3,
    "userId": 4
  },
  {
    "id": 2,
    "name": "sleep",
    "degree": 0,
    "userId": 4
  }
]
```

If the tags have not been added yet:
```http request
HTTP/1.1 204 No Content
Content-Type: application/json
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer
```

## 2. Adding a new mood tag
### Example
```http request
POST https://moodtracker.ru/api/mood_tag
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJJdmFuIiwiZXhwIjoxNzE3MDY1MzUzLCJkZXYiOiJNWV9ERVZJQ0UiLCJ1c2VySWQiOiI0In0.93im0syhNWJ8OOxUasa5MWqjfiTnIMkQkBnVfGV8CJJa87JSI18ZniWUasuZQp64HqAM6aj8KBgseT4TBjdpXw

{
  "name": "girl friend",
  "degree": 3
}
```

### Successful response
```http request
HTTP/1.1 201 Created
Content-Type: application/json
Content-Length: 44
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer

{
  "id": 1,
  "name": "girl friend",
  "degree": 3,
  "userId": 4
}
```

### Errors
Re-adding a tag with the same name and _user_id_:
```http request
HTTP/1.1 409 Conflict
Content-Type: application/json
Content-Length: 133
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer

{
  "timestamp": "2024-05-30T13:07:10.032477",
  "status": 409,
  "error": "Conflict",
  "message": "Mood tag already exists",
  "path": "/api/mood_tag"
}
```
_user_id_ is determined by the Bearer token that is passed along with the request

## 3. Changing a specific mood tag
### Example
Changing the degree of mood in the tag from 3 to 4

```http request
PUT https://moodtracker.ru/api/mood_tag
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJJdmFuIiwiZXhwIjoxNzE3MDY1MzUzLCJkZXYiOiJNWV9ERVZJQ0UiLCJ1c2VySWQiOiI0In0.93im0syhNWJ8OOxUasa5MWqjfiTnIMkQkBnVfGV8CJJa87JSI18ZniWUasuZQp64HqAM6aj8KBgseT4TBjdpXw

{
  "id": 1,
  "name": "girl friend",
  "degree": 4
}
```

### Successful response
```http request
HTTP/1.1 204 No Content
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer
```

An invalid tag id was specified when the data was changed: 
### Errors
```http request
HTTP/1.1 404 Not Found
Content-Type: application/json
Content-Length: 147
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer

{
  "timestamp": "2024-05-30T13:38:55.914027",
  "status": 404,
  "error": "Not Found",
  "message": "Mood tag not found from the database",
  "path": "/api/mood_tag"
}
```

## 4. Deleting a mood tag
### Example
```http request
DELETE https://moodtracker.ru/api/mood_tag?id=1
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJJdmFuIiwiZXhwIjoxNzE3MDY3MjYzLCJkZXYiOiJNWV9ERVZJQ0UiLCJ1c2VySWQiOiIxIn0.HE13E5tbLktTnYq7LGPWJNFwPJNuAcoR-f1BL1hWpWSDPrRTXBOXM8gZA7HrIteDFWwtV4SKyK2e0dTmtNiXlw
```

### Successful response
```http request
HTTP/1.1 204 No Content
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer
```

### Errors
The id of a nonexistent tag is specified:
```http request
HTTP/1.1 404 Not Found
Content-Type: application/json
Content-Length: 147
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer

{
  "timestamp": "2024-05-30T13:41:35.187085",
  "status": 404,
  "error": "Not Found",
  "message": "Mood tag not found from the database",
  "path": "/api/mood_tag"
}
```

## Mood entry
## 1. Getting a mood state
### Example
```http request
GET https://moodtracker.ru/api/mood_entry?start_date=2024-04-01&end_date=2024-04-18
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJJdmFuIiwiZXhwIjoxNzE3MDY3MjYzLCJkZXYiOiJNWV9ERVZJQ0UiLCJ1c2VySWQiOiIxIn0.HE13E5tbLktTnYq7LGPWJNFwPJNuAcoR-f1BL1hWpWSDPrRTXBOXM8gZA7HrIteDFWwtV4SKyK2e0dTmtNiXlw
```

### Successful responses
No mood states were found for the selected date range: 
```http request
HTTP/1.1 204 No Content
Content-Type: application/json
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer
```
This method returns a successful response with the status: NO_CONTENT

Mood states for the specified range are found:
```http request
HTTP/1.1 200 OK
transfer-encoding: chunked
Content-Type: application/json
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer

[
  {
    "id": 1,
    "degree": 1,
    "user_id": 1,
    "date_time": "2024-05-28T11:23:51",
    "mood_tags": [
      {
        "id": 2,
        "name": "sleep",
        "degree": 0,
        "userId": 1
      },
      {
        "id": 3,
        "name": "friends",
        "degree": 3,
        "userId": 1
      }
    ]
  }
]
```

### Errors
## 3. Maintaining a new mood state
While saving the mood state, you can also add new tags or change old ones. To add new tags, simply specify the data that the user wants to save. To change the old ones, you need to specify the tag id and new data

### Example
```http request
POST https://moodtracker.ru/api/mood_entry
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJJdmFuIiwiZXhwIjoxNzE3MDY3MjYzLCJkZXYiOiJNWV9ERVZJQ0UiLCJ1c2VySWQiOiIxIn0.HE13E5tbLktTnYq7LGPWJNFwPJNuAcoR-f1BL1hWpWSDPrRTXBOXM8gZA7HrIteDFWwtV4SKyK2e0dTmtNiXlw

{
  "degree": 1,
  "date_time": "2024-05-28T11:23:51",
  "mood_tags": [
    {
      "name": "friends",
      "degree": 3
    },
    {
      "id": 2,
      "name": "sleep",
      "degree": 0
    }
  ]
}
```

### Successful response
```http request
HTTP/1.1 201 Created
Content-Type: application/json
Content-Length: 173
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer

{
  "id": 1,
  "degree": 1,
  "user_id": 1,
  "date_time": "2024-05-28T11:23:51",
  "mood_tags": [
    {
      "id": 2,
      "name": "sleep",
      "degree": 0,
      "userId": 1
    },
    {
      "id": 3,
      "name": "friends",
      "degree": 3,
      "userId": 1
    }
  ]
}
```

### Errors
An attempt to add a tag with a name that already exists. Or change a tag whose name conflicts with others:
```http request
HTTP/1.1 400 Bad Request
Content-Type: application/json
Content-Length: 154
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer

{
  "timestamp": "2024-05-30T13:46:37.872068",
  "status": 400,
  "error": "Bad Request",
  "message": "Mood entry was not save in the database",
  "path": "/api/mood_entry"
}
```

## 3. Changing the mood tag
### Example
In order to change the mood tag, you need to specify its id and the data that you want to change. 
To change tags, you also need to specify their id. If you need to add a new tag, you need to specify the data for it without an id

```http request
PUT https://moodtracker.ru/api/mood_entry
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJJdmFuIiwiZXhwIjoxNzE3MDY3MjYzLCJkZXYiOiJNWV9ERVZJQ0UiLCJ1c2VySWQiOiIxIn0.HE13E5tbLktTnYq7LGPWJNFwPJNuAcoR-f1BL1hWpWSDPrRTXBOXM8gZA7HrIteDFWwtV4SKyK2e0dTmtNiXlw

{
  "id": 3,
  "degree": 3,
  "date_time": "2024-05-28T12:01:02",
  "mood_tags": [
    {
      "name": "impro",
      "degree": 3
    },
    {
      "id": 2,
      "name": "sleep",
      "degree": 1
    },
    {
      "name": "friends",
      "degree": 4
    }
  ]
}
```

### Successful response
```http request
HTTP/1.1 204 No Content
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer
```
The method returns the successful status: NO_CONTENT

### Errors
When trying to create or change the tag name to one that already exists for the user:
```http request
HTTP/1.1 400 Bad Request
Content-Type: application/json
Content-Length: 156
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer

{
  "timestamp": "2024-05-30T13:53:41.243552",
  "status": 400,
  "error": "Bad Request",
  "message": "Mood entry was not update in the database",
  "path": "/api/mood_entry"
}
```

## 4. Deleting a mood tag
### Example
```http request
DELETE https://moodtracker.ru/api/mood_entry?id=1
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJJdmFuIiwiZXhwIjoxNzE3MDY3MjYzLCJkZXYiOiJNWV9ERVZJQ0UiLCJ1c2VySWQiOiIxIn0.HE13E5tbLktTnYq7LGPWJNFwPJNuAcoR-f1BL1hWpWSDPrRTXBOXM8gZA7HrIteDFWwtV4SKyK2e0dTmtNiXlw
```

### Successful response
```http request
HTTP/1.1 204 No Content
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer
```
The method returns the successful status: NO_CONTENT

### Errors
When specifying a non-existent _id_ mood state:
```http request
HTTP/1.1 404 Not Found
Content-Type: application/json
Content-Length: 151
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer

{
  "timestamp": "2024-05-30T13:56:57.633664",
  "status": 404,
  "error": "Not Found",
  "message": "Mood entry not found from the database",
  "path": "/api/mood_entry"
}
```

### Additional links to documentation
For more information, please refer to the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.1.5/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.1.5/maven-plugin/reference/html/#build-image)