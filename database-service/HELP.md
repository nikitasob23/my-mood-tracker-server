# Database microservice

Open API v1.0

## Configuration:
```
server:
   hostname: {hostname}
   port: {port_num}
   servlet:
      context-path: /api/service
```

## Work with user's data:

## 1. Get User Info
   
   #### Authorization:
   Авторизация
   #### GET Request
   ```
   GET http://localhost:8081/api/service/user?username={username}
   ```

   #### Path params:
   _username_ - имя пользователя, данные которого нужно получить
   
   #### Response samples:
   ```
   HTTP/1.1 200
   {
      "username": "myusername",
      "nickname": "mynickname",
      "password": "mypassword"
   }
   ```
   ```
   HTTP/1.1 403
   {
      "timestamp": "2023-11-05T19:16:35.980414",
      "status": 403,
      "error": "Forbidden",
      "message": "User is trying to access someone else's data",
      "path": "/api/service/user"
   }
   ```
   ```
   HTTP/1.1 500
   {
      "timestamp": "2023-11-05T16:19:43.737+00:00",
      "status": 500,
      "error": "Internal Server Error",
      "path": "/api/service/user"
   }
   ```

## 2. Add New User Info:
   
   ### Authorization:
   Авторизация

   ### POST Request
   ```
   POST http://localhost:8081/api/service/user
   ```

   ### Request sample:
   ```
   {
     "username": "TEST_USERNAME",
     "nickname": "TEST_NICKNAME",
     "password": "TEST_PASSWORD"
   }
   ```
   ### Request Body schema:
   _username_ - имя пользователя
   _nickname_ - псевдоним пользователя
   _password_ - пароль пользователя

   ### Response samples:
   ```
   HTTP/1.1 201 
   Content-Length: 0
   Date: Sun, 05 Nov 2023 17:50:06 GMT
   Keep-Alive: timeout=60
   Connection: keep-alive`
   ```
   ```
   HTTP/1.1 500
   {
     "timestamp": "2023-11-05T17:53:13.782+00:00",
     "status": 500,
     "error": "Internal Server Error",
     "path": "/api/service/user"
   }
   ```






### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.1.5/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.1.5/maven-plugin/reference/html/#build-image)

