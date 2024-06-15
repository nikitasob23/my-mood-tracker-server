# Database-service API Guide
Open API v1.0.0

## Overview
This microservice provides a REST API for the Mood tracker application to interact with the database.

## Contents
1. [Dependencies](#dependencies)
2. [Core Entities](#core-entities)
3. [REST Methods](#rest-methods)
    - [User](#user)
    - [MoodTag](#moodtag)
    - [MoodEntry](#moodentry)
4. [Documentation Links](#documentation-links)

## Dependencies
The database-service project utilizes the following Maven dependencies to ensure functionality, database management, caching, logging, and other key aspects of the service:

### Spring Boot
1. **Spring Boot Starter** - enables Spring functionalities
2. **Spring Boot Starter Test** - library for testing support
3. **Spring Boot Starter Web** - provides necessary tools for web functionalities: RESTful controllers, servlets, etc.
4. **Spring Web Flux** - framework for asynchronous and reactive web request handling
5. **Spring Data JPA** - enables database interactions through automatic repository creation and other abstractions
6. **Spring Cache** - enables data caching from the database and other auxiliary values
7. **Spring Data Redis** - NoSQL storage for application caching
8. **Spring Cloud Config Client** - client for retrieving microservice configurations

### Logging
1. **Logstash Logback Encoder** - library for logging data in a specific format
2. **Logger** - module implementing a custom logger. This logger adds object state to the log messages.

### Miscellaneous
1. **Lombok** - reduces boilerplate code
2. **MapStruct** - framework for mapping different models and entities, reducing code and potential errors in data conversion

### Data Handling
1. **MySQL Connector J** - driver for connecting to MySQL databases
2. **Flyway MySQL** - tool for database versioning and migration

### Auxiliary Modules
1. **Domain** - module containing core models and auxiliary components for microservice functionality
2. **Layer Connector** - module that provides automatic model mapping between different application layers

## Configuration
### 1. Configuration Retrieval
The microservice can retrieve configuration from the config service, so you can specify the service name and the address where the configuration request will be sent.
```yaml
spring:
  application:
    name: [APP_NAME]
  config:
    import: configserver:[CONFIG_SERVER_ADDRESS]
```

### 2. Cache Connection
The microservice uses Redis as a cache store. Connection details:
```yaml
spring:
  data:
    redis:
      host: [HOST]
      port: [PORT]
```

### 3. Database Connection
```yaml
spring:
  datasource:
    url: jdbc:mysql://[HOST]:[PORT]/[DB_NAME]
    username: [USERNAME]
    password: [PASSWORD]
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### 4. Logging
Since the project uses a custom logger: ObjectStateLogger, which logs not only messages but also the state of the specified object, it is important to specify the factory for generating the logger. Additionally, specify the field names that the logger should mask when logging the object's state.
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

### 5. Connection Details
```yaml
microservice:
  connection:
    database:
      protocol: [http/https]
      hostname: [HOST]
      port: [PORT]
      path: /api/service/database

server:
  port: ${microservice.connection.database.port}
  servlet:
    context-path: ${microservice.connection.database.path}
```

### 6. Other Settings
Since there can be many mood entries in the database, retrieval is possible only by date range. To specify the default date range, use the following settings:
```yaml
service:
  loading:
    mood-entry:
      def-date-interval-days: 15
```

## Core Entities
### 1. User
Entity containing core user data, including authorization and business model data.
```java
public class UserEntity {
    private Long id;
    private String email;
    private String username;
    private String encodedPassword;

    private Set<MoodEntryEntity> moodEntries;
    private Set<MoodTagEntity> moodTags;
    private Set<AuthTokenEntity> authTokens;
}
```

### 2. MoodEntry
Entity representing a snapshot of mood state at a specific point in time. Its main parameter is the mood degree.
```java
public class MoodEntryEntity {
    private Long id;
    private int degree;
    private LocalDateTime dateTime;
    private Long userId;
    
    private UserEntity user;
    private Set<MoodTagEntity> moodTags = new HashSet<>();
}
```

### 3. MoodTag
Entity characterizing mood states. These can be people or circumstances affecting the state, such as friends or work.
```java
public class MoodTagEntity {
    private Long id;
    private String name;
    private int degree;
    private Long userId;
    
    private UserEntity user;
    private Set<MoodEntryEntity> moodEntries;
}
```

## REST Methods
In production, the API of this microservice is intended for interaction only within internal networks. The microservice does not imply authorization or authentication. This is handled by the auth-service within the application. The service sends and receives data only in JSON format.

## User

### 1. Check if a user exists by email
### Request Example
GET http://localhost:8082/api/user?username=Ivan

### Successful Response:
```http request
HTTP/1.1 200 
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer
Content-Type: application/json
Content-Length: 132
Date: Tue, 28 May 2024 12:19:48 GMT
Keep-Alive: timeout=60
Connection: keep-alive

{
  "id": 3,
  "email": "lagecah864@adrais.com",
  "username": "Ivan",
  "password": "$2a$08$JUlzlFijRiKpz41FaOZOuenUMUq8XKB1KJZULq1.zYgXu.RJqWsI6"
}
```
**User not found:**
```
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 21 May 2024 20:01:02 GMT
Keep-Alive: timeout=60
Connection: keep-alive

false
```

## 2. Retrieve User
### Request Example

GET http://127.0.0.1:8081/api/service/database/user?username=Ivan

### Successful Response
```http request
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 21 May 2024 14:56:32 GMT
Keep-Alive: timeout=60
Connection: keep-alive

{
  "id": 1,
  "email": "email@mail.com",
  "username": "Ivan",
  "password": "SECRET"
}
```

### Errors
User not found by _username_:
```http request
HTTP/1.1 404 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 21 May 2024 20:02:39 GMT
Keep-Alive: timeout=60
Connection: keep-alive

{
  "timestamp": "2024-05-21T20:02:39.391330998",
  "status": 404,
  "error": "Not Found",
  "message": "The user was not found",
  "path": "/api/service/database/user"
}
```

## 3. Retrieve Full User with Mood States and Tags

### Request Example
GET http://127.0.0.1:8081/api/service/database/user/full?username=Ivan

### Successful Response
```http request
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 21 May 2024 17:10:14 GMT
Keep-Alive: timeout=60
Connection: keep-alive

{
  "id": 1,
  "email": "email@mail.com",
  "username": "Ivan",
  "password": "SECRET",
  "mood_entries": [
    {
      "id": 1,
      "degree": 1,
      "user_id": 1,
      "date_time": "2024-05-21T23:07:38",
      "mood_tag_ids": [
        "1",
        "2"
      ]
    }
  ],
  "mood_tags": [
    {
      "id": 1,
      "name": "work",
      "degree": 3,
      "userId": 1
    },
    {
      "id": 2,
      "name": "sleep",
      "degree": 0,
      "userId": 1
    }
  ]
}
```

### Errors
User not found by _username_:
```http request
HTTP/1.1 404 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 21 May 2024 20:02:39 GMT
Keep-Alive: timeout=60
Connection: keep-alive

{
  "timestamp": "2024-05-21T20:02:39.391330998",
  "status": 404,
  "error": "Not Found",
  "message": "The user was not found",
  "path": "/api/service/database/user"
}
```

## 4. Add New User:

### Request Example
POST http://127.0.0.1:8081/api/service/database/user

**Body:**
```http request
Content-Type: application/json

{
  "email": "IvanIvanov@mail.com",
  "username": "Ivan",
  "password": "SECRET"
}
```

### Successful Response
```http request
HTTP/1.1 201 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 21 May 2024 17:17:37 GMT
Keep-Alive: timeout=60
Connection: keep-alive

{
  "id": 1,
  "email": "IvanIvanov@mail.com",
  "username": "Ivan",
  "password": "SECRET",
  "mood_entries": null,
  "mood_tags": null
}
```

### Errors
User already exists:
```http request
HTTP/1.1 409 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 21 May 2024 20:26:38 GMT
Keep-Alive: timeout=60
Connection: keep-alive

{
  "timestamp": "2024-05-21T20:26:38.307457012",
  "status": 409,
  "error": "Conflict",
  "message": "user already exists",
  "path": "/api/service/database/user"
}
```

User did not send all data:
```http request
HTTP/1.1 400 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 21 May 2024 20:27:20 GMT
Connection: close

{
  "timestamp": "2024-05-21T20:27:20.522511652",
  "status": 400,
  "error": "Bad Request",
  "message": "User has not saved",
  "path": "/api/service/database/user"
}
```

## 5. Update User:
### Request Example
PUT http://127.0.0.1:8081/api/service/database/user

**Body:**
```http request
Content-Type: application/json

{
  "id": 4,
  "email": "IvanIvanov@gmail.com",
  "username": "Ivan",
  "password": "TOTAL_SECRET"
}
```

### Successful Response

This request returns a successful status: NO_CONTENT with an empty response body
```http request
HTTP/1.1 204 
Date: Tue, 21 May 2024 19:37:58 GMT
Keep-Alive: timeout=60
Connection: keep-alive
```

### Errors
User not found:
```http request
HTTP/1.1 404 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 21 May 2024 20:20:26 GMT
Keep-Alive: timeout=60
Connection: keep-alive

{
  "timestamp": "2024-05-21T20:20:26.898956783",
  "status": 404,
  "error": "Not Found",
  "message": "The user was not found",
  "path": "/api/service/database/user"
}
```
User did not send all data:
```http request
HTTP/1.1 400 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 21 May 2024 20:21:44 GMT
Connection: close

{
  "timestamp": "2024-05-21T20:21:44.169085994",
  "status": 400,
  "error": "Bad Request",
  "message": "User has not updated",
  "path": "/api/service/database/user"
}
```

## 6. Delete User:
### Request Example

DELETE http://127.0.0.1:8081/api/service/database/user?username=Ivan

### Successful Response
This request returns a successful status: NO_CONTENT with an empty response body
```http request
HTTP/1.1 204 
Date: Tue, 21 May 2024 19:42:29 GMT
Keep-Alive: timeout=60
Connection: keep-alive
```

### Errors
```http request
HTTP/1.1 404 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 21 May 2024 20:28:55 GMT
Keep-Alive: timeout=60
Connection: keep-alive

{
  "timestamp": "2024-05-21T20:28:55.607842633",
  "status": 404,
  "error": "Not Found",
  "message": "The user was not found",
  "path": "/api/service/database/user"
}
```

## MoodTag

###1. Getting a mood tag
### Request example
GET http://127.0.0.1:8081/api/service/database/mood_tag?user_id=9
### Successful response
```http request
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 21 May 2024 20:54:49 GMT
Keep-Alive: timeout=60
Connection: keep-alive
[
  {
    "id": 3,
    "name": "home",
    "degree": 3,
    "userId": 9
  },
  {
    "id": 4,
    "name": "girl friend",
    "degree": 4,
    "userId": 9
  }
]
```
If the token does not exist, the status is returned: NO_CONTENT with an empty response body
```http request
HTTP/1.1 204 
Content-Type: application/json
Date: Tue, 21 May 2024 20:53:09 GMT
Keep-Alive: timeout=60
Connection: keep-alive
```
### Errors
If the user who owns the tag does not exist:
```http request
HTTP/1.1 404 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 21 May 2024 20:55:27 GMT
Keep-Alive: timeout=60
Connection: keep-alive
{
  "timestamp": "2024-05-21T20:55:27.883609884",
  "status": 404,
  "error": "Not Found",
  "message": "User not found",
  "path": "/api/service/database/mood_tag"
}
```
###2. Adding a new mood tag
### Request example
POST http://127.0.0.1:8081/api/service/database/mood_tag
**body:**
```http request
Content-Type: application/json
{
  "name": "moves",
  "degree": 4,
  "userId": 11
}
```
### Successful response
```http request
HTTP/1.1 201 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 21 May 2024 21:04:18 GMT
Keep-Alive: timeout=60
Connection: keep-alive
{
  "id": 6,
  "name": "moves",
  "degree": 4,
  "userId": 11
}
```
### Errors
The tag already exists:
```http request
HTTP/1.1 409 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 21 May 2024 21:05:21 GMT
Keep-Alive: timeout=60
Connection: keep-alive
{
  "timestamp": "2024-05-21T21:05:21.130307364",
  "status": 409,
  "error": "Conflict",
  "message": "Mood tag entity already exists",
  "path": "/api/service/database/mood_tag"
}
```
The user they are trying to add the tag to does not exist:
```http request
HTTP/1.1 404 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 21 May 2024 21:07:04 GMT
Keep-Alive: timeout=60
Connection: keep-alive
{
  "timestamp": "2024-05-21T21:07:04.639999771",
  "status": 404,
  "error": "Not Found",
  "message": "User not found",
  "path": "/api/service/database/mood_tag"
}
```
###3. Updating the mood tag
### Request example
PUT http://127.0.0.1:8081/api/service/database/mood_tag
**body:**
```http request
Content-Type: application/json
{
  "id": 8,
  "name": "friends",
  "degree": 3,
  "userId": 12
}
```
### Successful response
In case of a successful attempt to update the tag, the successful status is returned: NO_CONTENT with an empty response body
```http request
HTTP/1.1 204 
Date: Tue, 21 May 2024 21:15:38 GMT
Keep-Alive: timeout=60
Connection: keep-alive
```
### Errors
The user whose tag they are trying to delete does not exist:
```http request
HTTP/1.1 404 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 21 May 2024 21:11:36 GMT
Keep-Alive: timeout=60
Connection: keep-alive
{
  "timestamp": "2024-05-21T21:11:36.988615630",
  "status": 404,
  "error": "Not Found",
  "message": "User not found",
  "path": "/api/service/database/mood_tag"
}
```
The tag does not exist:
```http request
HTTP/1.1 404 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 21 May 2024 21:14:17 GMT
Keep-Alive: timeout=60
Connection: keep-alive
{
  "timestamp": "2024-05-21T21:14:17.902811650",
  "status": 404,
  "error": "Not Found",
  "message": "The mood tag was not found",
  "path": "/api/service/database/mood_tag"
}
```
### 3. Deleting a mood tag
### Request example
DELETE http://127.0.0.1:8081/api/service/database/mood_tag?id=8&user_id=12
### Successful response
In case of a successful attempt to update the tag, the successful status is returned: NO_CONTENT with an empty response body
```http request
HTTP/1.1 204 
Date: Tue, 21 May 2024 21:23:22 GMT
Keep-Alive: timeout=60
Connection: keep-alive
```
### Errors
The user whose tag they are trying to delete does not exist:
```http request
HTTP/1.1 404 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 21 May 2024 21:22:00 GMT
Keep-Alive: timeout=60
Connection: keep-alive
{
  "timestamp": "2024-05-21T21:22:00.299286118",
  "status": 404,
  "error": "Not Found",
  "message": "User not found",
  "path": "/api/service/database/mood_tag"
}
```
The tag does not exist:
```http request
HTTP/1.1 404 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 21 May 2024 21:22:30 GMT
Keep-Alive: timeout=60
Connection: keep-alive
{
  "timestamp": "2024-05-21T21:22:30.080287882",
  "status": 404,
  "error": "Not Found",
  "message": "The mood tag was not found",
  "path": "/api/service/database/mood_tag"
}
```

## MoodEntry

###1. Getting a mood tag
### Request example
GET http://127.0.0.1:8081/api/service/database/mood_entry?user_id=4&start_date=2024-05-01&end_date=2024-05-23
### Successful response
```http request
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 21 May 2024 20:54:49 GMT
Keep-Alive: timeout=60
Connection: keep-alive
[
  {
    "id": 3,
    "name": "home",
    "degree": 3,
    "userId": 9
  },
  {
    "id": 4,
    "name": "girl friend",
    "degree": 4,
    "userId": 9
  }
]
```
If the status does not exist, the status is returned: NO_CONTENT with an empty response body
```http request
HTTP/1.1 204 
Content-Type: application/json
Date: Tue, 21 May 2024 21:26:38 GMT
Keep-Alive: timeout=60
Connection: keep-alive
```
### Errors
If the user who owns the state does not exist:
```
HTTP/1.1 404 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 21 May 2024 21:26:06 GMT
Keep-Alive: timeout=60
Connection: keep-alive
{
  "timestamp": "2024-05-21T21:26:06.646318574",
  "status": 404,
  "error": "Not Found",
  "message": "User not found",
  "path": "/api/service/database//mood_entry"
}
```
###2. Adding a new mood state
### Request example
POST http://127.0.0.1:8081/api/service/database/mood_entry
**body:**
```
Content-Type: application/json
{
  "degree": 1,
  "user_id": 12,
  "date_time": "2024-04-10T23:07:38"
}
```
### Successful response
```
HTTP/1.1 201 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 21 May 2024 21:40:49 GMT
Keep-Alive: timeout=60
Connection: keep-alive
{
  "id": 2,
  "degree": 1,
  "user_id": 12,
  "date_time": "2024-04-10T23:07:38",
  "mood_tags": []
}
```
You can also add a new mood tag or edit an old one with the state.
If you need to attach an old tag and update it, then you need to specify the tag id in the request along with the rest of the data.
If you need to add a new tag, it is enough to specify only the required fields: _name_, _user id_
POST http://127.0.0.1:8081/api/service/database/mood_entry
**body:**
```
Content-Type: application/json
{
  "degree": 1,
  "user_id": 12,
  "date_time": "2024-05-21T23:07:38",
  "mood_tags": [
    {
      "id": 9,
      "name": "Computer games",
      "degree": 3,
      "userId": 12
    },
    {
      "name": "Sleep",
      "degree": 0,
      "userId": 12
    }
  ]
}
```
### Successful response
```
HTTP/1.1 201 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 21 May 2024 21:46:46 GMT
Keep-Alive: timeout=60
Connection: keep-alive
{
  "id": 3,
  "degree": 1,
  "user_id": 12,
  "date_time": "2024-05-21T23:07:38",
  "mood_tags": [
    {
      "id": 9,
      "name": "Computer games",
      "degree": 3,
      "userId": 12
    },
    {
      "id": 10,
      "name": "Sleep",
      "degree": 0,
      "userId": 12
    }
  ]
}
```
### Errors
The tag that was added to the mood state cannot be updated because it cannot be found:
```
HTTP/1.1 400 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 21 May 2024 21:45:39 GMT
Connection: close
{
  "timestamp": "2024-05-21T21:45:39.769223574",
  "status": 400,
  "error": "Bad Request",
  "message": "The tag that needs to be updated was not found",
  "path": "/api/service/database/mood_entry"
}
```
An attempt to re-add a tag that has already been created through the mood state:
```
HTTP/1.1 400 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 21 May 2024 22:47:20 GMT
Connection: close
{
  "timestamp": "2024-05-21T22:47:20.942831708",
  "status": 400,
  "error": "Bad Request",
  "message": "Mood tag entity has not updated",
  "path": "/api/service/database/mood_entry"
}
```
Adding a mood state when the user has not yet been created:
```
HTTP/1.1 404 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 21 May 2024 22:50:28 GMT
Keep-Alive: timeout=60
Connection: keep-alive
{
  "timestamp": "2024-05-21T22:50:28.561393551",
  "status": 404,
  "error": "Not Found",
  "message": "User not found",
  "path": "/api/service/database/mood_entry"
}
```
###3. Mood Status Update
### Request example
PUT http://127.0.0.1:8081/api/service/database/mood_entry
**body:**
```
Content-Type: application/json
{
  "id": 9,
  "degree": 3,
  "user_id": 12,
  "date_time": "2024-05-19T23:59:02",
  "mood_tags": [
    {
      "name": "impro",
      "degree": 3,
      "userId": 12
    },
    {
      "id": 21,
      "name": "Sleep",
      "degree": 1,
      "userId": 12
    },
    {
      "name": "friends",
      "degree": 4,
      "userId": 12
    }
  ]
}
```
### Successful response
In case of a successful attempt to update the tag, the successful status is returned: NO_CONTENT with an empty response body
```
HTTP/1.1 204 
Date: Tue, 21 May 2024 23:10:00 GMT
Keep-Alive: timeout=60
Connection: keep-alive
```
### Errors
The tag that was added to the mood state cannot be updated because it cannot be found:
```
HTTP/1.1 400 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 21 May 2024 21:45:39 GMT
Connection: close
{
  "timestamp": "2024-05-21T21:45:39.769223574",
  "status": 400,
  "error": "Bad Request",
  "message": "The tag that needs to be updated was not found",
  "path": "/api/service/database/mood_entry"
}
```
An attempt to re-add a tag that has already been created through the mood state:
```
HTTP/1.1 400 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 21 May 2024 22:51:56 GMT
Connection: close
{
  "timestamp": "2024-05-21T22:51:56.515041527",
  "status": 400,
  "error": "Bad Request",
  "message": "Mood tag entity has not updated",
  "path": "/api/service/database/mood_entry"
}
```
Adding a mood state when the user has not yet been created:
```
HTTP/1.1 404 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 21 May 2024 22:50:28 GMT
Keep-Alive: timeout=60
Connection: keep-alive
{
  "timestamp": "2024-05-21T22:50:28.561393551",
  "status": 404,
  "error": "Not Found",
  "message": "User not found",
  "path": "/api/service/database/mood_entry"
}
```
Adding another user's tag to one user's mood state:
```
HTTP/1.1 400 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 21 May 2024 23:12:40 GMT
Connection: close
{
  "timestamp": "2024-05-21T23:12:40.281634041",
  "status": 400,
  "error": "Bad Request",
  "message": "Mood tag entities not merged because they have different ids",
  "path": "/api/service/database/mood_entry"
}
```
### 3. Deleting a mood state
### Request example
DELETE http://127.0.0.1:8081/api/service/database/mood_entry?id=9
### Successful response
In case of a successful attempt to update the mood state, the successful status is returned: NO_CONTENT with an empty response body
```http request
HTTP/1.1 204 
Date: Tue, 21 May 2024 23:16:41 GMT
Keep-Alive: timeout=60
Connection: keep-alive
```

### Errors
There is no mood state:
```http request
HTTP/1.1 404 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 21 May 2024 21:22:30 GMT
Keep-Alive: timeout=60
Connection: keep-alive
{
  "timestamp": "2024-05-21T21:22:30.080287882",
  "status": 404,
  "error": "Not Found",
  "message": "The mood tag was not found",
  "path": "/api/service/database/mood_tag"
}
```
### Documentation Links
For more information, please refer to the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.1.5/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.1.5/maven-plugin/reference/html/#build-image)