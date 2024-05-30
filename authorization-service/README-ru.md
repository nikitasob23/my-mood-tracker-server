# Руководство API сервиса authorization-service
Open API v1.0.0

## Обзор:
Данный микросервис предоставляет REST API для регистрации, авторизации и аутентификации пользователей. Регистрация проходит с подтверждением почты

## Содержание
1. [Зависимости](#зависимости)
2. [Конфигурация](#конфигурация)
3. [REST методы](#rest-методы)
    - [Регистрация](#1-регистрация)
    - [Подтверждение почты](#2-подтверждение-почты-)
    - [Авторизация по логину и паролю](#3-авторизация-по-логину-и-паролю)
    - [Авторизация по refresh token](#авторизация-по-refresh-token)
    - [Выход из системы](#4-выход-из-системы)
4. [Документации](#дополнительные-ссылки-на-документацию)

## Зависимости:
Проект database-service использует следующие Maven зависимости для обеспечения функциональности, авторизации, кэширования, логирования, и других ключевых аспектов сервиса:

### Spring boot:
1. **Spring Boot Starter** - зависимость, обеспечивающая работу Spring
2. **Spring boot starter test** - библиотека для работы с тестами
3. **spring boot starter web** - предоставляет все необходимые инструменты для веб-функционала: RESTful контроллеров, сервлетов и тд
4. **spring Web Flux** - фреймворк, использующийся для асинхронной и реактивной обработки веб-запросов
5. **Spring Cache** - предоставляет интерфейс для кэширования вспомогательных данных при авторизации
6. **Spring Data Redis** - NoSQL хранилище для кэша приложения
7. **Spring cloud config client** - клиент, обеспечивающий получение конфигурации для микросервиса

### Авторизация:
1. **Spring security crypto** - модуль библиотеки Spring Security, предоставляющий функционал для криптографических операций. В приложении используется для шифрования и проверки паролей пользователей. Этот модуль может использоваться независимо от остальных компонентов Spring Security
2. **jjwt api** и **jjwt-impl** - библиотеки для работы с JSON Web Token, на котором основана авторизация в микросервисе
3. **jjwt-jackson** и **gson** -  библиотеки, предоставляющие функционал для работы с JSON

### Логирование:
1. **Logstash Logback Encoder** - библиотека для логирования данных в определенном формате
2. **Logger** - модуль, реализующий работу кастомного логера. Данный логер добавляет к сообщениям состояние объекта

### Прочее:
1. **Lombok** - используется для уменьшения шаблонного кода
2. **MapStruct** - фреймворк для маппинга разных моделей и сущностей, уменьшая количество кода и потенциальных ошибок при преобразовании данных

### Вспомогательные модули:
1. **Domain** - модуль, содержащий основные модели и вспомогательные компоненты для функционирования микросервиса.
2. **Layer connector** - модуль, который обеспечивает автоматический маппинг моделей между разными словями приложения

## Конфигурация:
### 1. Получение конфигурации:
В микросервисе есть возможность получения конфигурации из config service, поэтому вы можете указать имя сервиса и адрес, по которому будет отправлен запрос на получение конфигурации
```
spring:
  application:
    name: [APP_NAME]
  config:
    import: configserver:[CONFIG_SERVER_ADDRESS]
```

### 2. Подключение кэша:
Микросервис использует redis в качестве кэш хранилища, данные для подключения:
```
spring:
  data:
    redis:
      host: [HOST]
      port: [PORT]
```

### 3. Логирование:
Так как в проекте используется кастомный логер: ObjectStateLogger, который логгирует не только сообщение, но и состояние указанного объекта, важно указать фабрику для генерации логгера. А так же названия тех полей, которые логгер должен маскировать при логгировании состояния объекта
```
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

### 4. Данные для подключения:
```
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

### 5. Настройка токенов авторизации:
```
auth:
  token:
    expiration-in-minutes:
      access: 5
      refresh: 21600
    secret-key:
      access: "SECRET"
      refresh: "SECRET"
```
1. **auth.token.expiration-in-minutes.access** - указывает, какое время жизни в минутах будет у access token.
2. **auth.token.expiration-in-minutes.refresh** - указывает, какое время жизни в минутах будет у refresh token.
3. **auth.token.secret-key** - настройки, содержащие секретные ключи для расшифровки токенов

_Access token_ - токен, который отправляет пользователь вместе с каждым запросом. Благодаря этому сервис понимает, кто к нему обращается. И на основании этой информации принимает решение, предоставлять доступ к данным или нет.

_Refresh token_ - токен, который отправляет пользователь для получения новой пары access-refresh токенов. Благодаря этому сервис понимает, кто хочет получить данные. И на основании этой информации принимает решение, выдавать новую пару или нет.

## Основные сущности
### 1. AuthToken
Основная модель микросервиса, которая содержит в себе **_access_** и **_refresh_** токены. А также **_device_** - привязанное устройство к паре токенов.
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
Модель, которую получает приложение для выполнения авторизации
```java
public class RowLoginInDetails {
    private final Username username;
    private final RowPassword rowPassword;
    private final String device;
}
```

### 3. SignupDetails
Модель для передачи данных во время регистрации
```java
public class SignupDetails {
    private final Email email;
    private final Username username;
    private final RowPassword rowPassword;
}
```

### 4. SignOutDetails
Модель с данным для выхода из системы  
```java
public class SignOutDetails {
    private final UserId userId;
    private final String device;
}
```

## REST методы
Микросервис занимается исключительно регистрацией авторизацией и аутентификацией пользователя. И не занимается безопасностью всего приложения. Для этих целей есть gateway-service. Сервис отдает и принимает данные только в формате JSON.

### 1. Регистрация
### Пример запроса
POST http://80.242.58.161:8092/api/service/auth/signup

**Body:**
```
Content-Type: application/json

{
  "email": "IvanIvanov@mail.com",
  "username": "Ivan",
  "password": "SECRET"
}
```
### Успешный ответ:
```
HTTP/1.1 201 
Content-Length: 0
Date: Wed, 22 May 2024 15:28:13 GMT
Keep-Alive: timeout=60
Connection: keep-alive
```

### Ошибки
Указана почта в некорректном формате:
```
{
  "timestamp": "2024-05-22T15:29:38.723125376",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid email",
  "path": "/api/service/auth"
}
```

### 2. Подтверждение почты 
После отправки регистрационных данных, на почту приходит ссылка с кодом активации

### Пример
```
https://80.242.58.161:8082/api/auth/signup/activate/2e288b77-2705-4719-9478-f99cc9bce21d
```
После перехода по этой ссылке, микросервис создает объект пользователя и сохраняет его в базе данных.

### 3. Авторизация по логину и паролю
### Пример
Процесс авторизации начинается с получения пары access-refresh токенов  

GET http://localhost:8092/api/service/auth/signout?userId=13&device=MY_DEVICE

### Успешный ответ
```
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
После получения, пара токенов сохраняется в базе данных. 
Access token необходимо указывать в заголовке каждого запроса в качестве Bearer для прохождения процесса авторизации и аутентификации при взаимодействии с данными.
### Ошибки
При указании неверного пароля:
```
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

При указании неверного username:
```
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

### 4. Авторизация по refresh token

POST http://80.242.58.161:8092/api/service/auth/token/refresh

**Body:**
```
Content-Type: application/json

{
  "refresh_token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJJdmFuIiwiZXhwIjoxNzE3NzUyNTk1LCJkZXYiOiJNWV9ERVZJQ0UiLCJ1c2VySWQiOiIxMyJ9.LsVS7NUdRV2frzfJHNcp8Vm2886MAugPiUiiG3il72oL1oUtiXOxBg1taucWC2axJjhR45jYFqeHh0cKY_S4bg"
}
```

### Успешный ответ
```
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

### Ошибки
При указании неверного refresh token:
```
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

### 4. Выход из системы
Данный метод удаляет пару access-refresh токенов для определенного устройства из базы данных. После этого они становятся недействительными 
### Пример
GET http://80.242.58.161:8092/api/service/auth/signout?userId=13&device=MY_DEVICE

**_Параметры:_**
- userId - идентификатор пользователя
- device - название устройства 

### Успешный ответ
Ответ возвращает успешный статус: NO_CONTENT
```
HTTP/1.1 204 
Date: Thu, 23 May 2024 09:41:07 GMT
Keep-Alive: timeout=60
Connection: keep-alive
```

### Ошибки
Несуществующий _user id_ или _device_:
```
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

### 5. Выход из системы со всех устройств
Данный метод удаляет пары access-refresh токенов для всех устройств из базы данных. После этого они становятся недействительными
### Пример
GET http://80.242.58.161:8092/api/service/auth/signout/all?userId=13

### Успешный ответ
```
HTTP/1.1 204 
Date: Thu, 23 May 2024 09:47:07 GMT
Keep-Alive: timeout=60
Connection: keep-alive
```
Данный метод возвращает успешный ответ даже если пользователя не существует

### Дополнительные ссылки на документацию
Для получения дополнительной информации, пожалуйста, ознакомьтесь со следующими разделами:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.1.5/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.1.5/maven-plugin/reference/html/#build-image)