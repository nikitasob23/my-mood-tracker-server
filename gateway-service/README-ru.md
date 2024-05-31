# Руководство по API gateway-service
Open API v1.0.0

## Обзор
gateway-service является входной точкой для всего приложения. Этот микросервис отвечает за безопасность и управление функциональностью других микросервисов.

## Содержание
1. [Зависимости](#зависимости)
2. [Конфигурация](#конфигурация)
3. [REST методы](#rest-методы)
   - [Управление учетной записью пользователя](#управление-учетной-записью-пользователя)
        - [Регистрация](#1-регистрация)
        - [Подтверждение почты после регистрации](#2-подтверждение-почты-после-регистрации)
        - [Авторизация по логину и паролю](#3-авторизация-по-логину-и-паролю)
        - [Авторизация по refresh token](#4-авторизация-по-refresh-token)
        - [Сброс пароля](#5-сброс-пароля)
        - [Изменение почты](#6-изменение-почты)
        - [Подтверждение почты после ее смены](#7-подтверждение-почты-после-ее-смены)
        - [Выход из системы](#8-выход-из-системы)
   - [Методы управления пользовательскими данными](#2-методы-управления-пользовательскими-данными)
     - [User](#user)
     - [Mood tag](#mood-tag)
     - [Mood entry](#mood-entry)
4. [Дополнительные ссылки на документацию](#дополнительные-ссылки-на-документацию)
   

## Зависимости
Проект gateway-service использует следующие Maven зависимости для обеспечения функциональности:

### Spring boot
1. **Spring Boot Starter** - зависимость, обеспечивающая работу Spring
2. **Spring boot starter test** - библиотека для работы с тестами
3. **spring Web Flux** - фреймворк, использующийся для асинхронной и реактивной обработки веб-запросов

### Безопасность
1. **Spring boot starter security** - модуль, который обеспечивает систему безопасности в приложении: защищает маршруты и тем самым управляет доступом к различным микросервисам.

### Логирование
1. **Logstash Logback Encoder** - библиотека для логирования данных в определенном формате
2. **Logger** - модуль, реализующий работу кастомного логера. Данный логер добавляет к сообщениям состояние объекта

### Прочее
1. **Lombok** - используется для уменьшения шаблонного кода
2. **MapStruct** - фреймворк для маппинга разных моделей и сущностей, уменьшая количество кода и потенциальных ошибок при преобразовании данных

### Вспомогательные модули
1. **Domain** - модуль, содержащий основные модели и вспомогательные компоненты для функционирования микросервиса.
2. **Layer connector** - модуль, который обеспечивает автоматический маппинг моделей между разными словями приложения

## Конфигурация
### 1. Получение конфигурации
В микросервисе есть возможность получения конфигурации из config service, поэтому вы можете указать имя сервиса и адрес, по которому будет отправлен запрос на получение конфигурации
```yaml
spring:
  application:
    name: [APP_NAME]
  config:
    import: configserver:[CONFIG_SERVER_ADDRESS]
```

### 2. Логирование
Так как в проекте используется кастомный логер: ObjectStateLogger, который логирует не только сообщение, но и состояние объекта, важно указать фабрику для генерации логера. А так же названия тех полей, которые логер должен маскировать при логировании состояния объекта
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

### 3. Данные для подключения
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

### Настройка безопасности Spring secure
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
### Пояснения
1. Отключается csrf-защита. В приложении она не нужна, поскольку авторизация происходит посредством передачи bearer в виде access token-а с каждым запросом
2. Отключатся форма входа в систему, так как приложение реализует именно REST API, сервер не настроен на выдачу html страниц
3. Запрещается доступ ко всем ресурсам, кроме сервисов регистрации и авторизации
4. Отключается базовая аутентификация по http, поскольку функцию аутентификации реализует bearer, который приходит с каждым запросом клиента
5. Добавляется менеджер авторизации, который производит авторизацию по access token
6. Добавляется контекст безопасности для сохранения авторизованного пользователя после получения bearer 
7. Добавляется фильтр AccessTokenFilter, который на основе bearer определяет, к каким ресурсам пользователь имеет доступ


## REST методы
Микросервис является точкой входа в приложение и отвечает за безопасность, фильтруя приходящий трафик

## Управление учетной записью пользователя
### 1. Регистрация
### Пример запроса
POST http://80.242.58.161:8082/api/auth/signup

**Body:**
```http request
Content-Type: application/json

{
  "email": "IvanIvanov@mail.com",
  "username": "Ivan",
  "password": "Very_secret0"
}
```
Требования к паролю: минимум одна буква в верхней регистре, минимум одна цифра. Длина пароля не менее 8 символов

### Успешный ответ
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

### Ошибки
Указана почта в некорректном формате:
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

Указан пароль в некорректном формате:
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

По указанному email или username уже была произведена регистрация:
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

### 2. Подтверждение почты после регистрации
Для того чтобы завершить процесс регистрации, нужно зайти на почту и перейти по отправленной ссылке с кодом активации

### Пример запроса
GET http://80.242.58.161:8082/api/auth/signup/activate/0da79d34-f9f3-48a4-bee2-9a56019fabea

### Успешный ответ
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

### Ошибки
Повторный переход по ссылке:
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

Указан неверный код активации в ссылке:
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

### 3. Авторизация по логину и паролю
### Пример
Процесс авторизации начинается с получения пары access-refresh токенов

POST http://80.242.58.161:8082/api/auth/token

**Body:**
```http request
Content-Type: application/json

{
"username": "Ivan",
"password": "Very_secret0",
"device": "MY_DEVICE"
}
```

### Успешный ответ
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

### Ошибки

Неверные username:
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

Неверный пароль:
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

### 4. Авторизация по refresh token
### Пример
POST http://80.242.58.161:8082:8082/api/auth/token/refresh

**Body:**
```http request
Content-Type: application/json

{
"refresh_token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJuaWtpdG9zMDAyM0BnbWFpbC5jb20iLCJleHAiOjE3MTQ5OTQ2NjYsInVzZXJJZCI6IjQiLCJkZXYiOiJURVNUX0RFVklDRSJ9.ZY0feVssnWkPTJFIhyrXuyr6M0Oit9Zl2FOLwAHExFDVF5Rqp63CU6tWQW3yNRfng4hjkaLAX3C0OaUT3i8YKQ"
}
```

### Успешный ответ
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

### Ошибки
Устаревший или некорректный refresh token:
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

После получения access token можно выполнять остальные запросы к сервису, указывая токен в качестве Bearer в заголовке авторизации.

**Если токен в запросе некоретный или устаревший:**
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

### 5. Сброс пароля
Изменить пароль можно только отправив запрос со старым паролем для проверки и новым паролем для смены
### Пример
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
Метод возвращает успешный статус: NO_CONTENT


### Ошибки
Старый пароль не совпадает с зашифрованным паролем в системе:
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

Новый пароль не соответствует требованиям безопасности:
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

### 6. Изменение почты
Изменить почту можно только после подтверждения новой
### Пример
Запрос на подверждение почты:
```http request
POST http://80.242.58.161:8082:8082/api/auth/reset/email
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJJdmFuIiwiZXhwIjoxNzE3MDU4NDk1LCJkZXYiOiJNWV9ERVZJQ0UiLCJ1c2VySWQiOiIxIn0.OycPsjgFgHqSyXWKBk131jb1CH5jqrujzimJpR0krWBNEI5j0AMlwxV1XhcPyH2NYsCYeA0RbfUQ1FBaLsfMbQ
Content-Type: application/json

{
  "email": "NewAndFreshIvanov@mail.com"
}
```

### Успешный ответ
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

### 7. Подтверждение почты после ее смены
### Пример
GET http://80.242.58.161:8082:8082/api/auth/reset/email/activate/35225058-e09b-4923-b0a8-de68514578b8

### Успешный ответ
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
Метод возвращает успешный статус: NO_CONTENT

### Ошибки
Код активации уже применен, но отправляется пользователем повторно:
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

### 8. Выход из системы
Данный метод удаляет пару access-refresh токенов для определенного устройства из системы. После этого они становятся недействительными
### Пример
```http request
GET http://80.242.58.161:8082/api/auth/signout?device=MY_DEVICE
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJJdmFuIiwiZXhwIjoxNzE2NTYxNjgxLCJkZXYiOiJNWV9ERVZJQ0UiLCJ1c2VySWQiOiIxNCJ9.iX2qWbsnDlwHXTS3xHlo0UMV8VlHTScBYvYkgmfsCJfnfNzHPl5TADwt94ert8sa9-603GSSXPmNdNDv9sb8Ig
```

### Успешный ответ
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

### Ошибки
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

Для удаления всех пар access-refresh токенов:
```http request
GET http://80.242.58.161:8082:8092/api/auth/signout/all
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJJdmFuIiwiZXhwIjoxNzE3MDU0NzY3LCJ1c2VySWQiOiIxIiwiZGV2IjoiTVlfREVWSUNFIn0.xwWPneNu9U7dO7IPDhYzAGmhArIYSKUQ-EjX8PEIx_vhl3yRuvFSk3SZomTMEk4Dn70sNrbJZEc_2WWx9BsdWA
```

### Успешный ответ
В ответе возвращается успешный статус: NO_CONTENT
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

### Ошибки
Метод может возвращать ошибку только в случае повторного выхода из системы из-за того, что токен стал недействительным:
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

## 2. Методы управления пользовательскими данными
## User
## 1. Получение пользователя
### Пример
```http request
GET http://80.242.58.161:8082:8082/api/user
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJJdmFuIiwiZXhwIjoxNzE3MDU1OTY4LCJ1c2VySWQiOiIxIiwiZGV2IjoiTVlfREVWSUNFIn0.nCFLz-Z1V9OdkiVHUcKW81q4UsW8jmA1CHme9nnM6A3zG7G0gEUuiuoaKPvTgzqx-l7c62EmK_FIfGmnQNwtAA
```

### Успешный ответ
```
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

## 2. Получение полных данных о пользователе
Метод возвращает пользователя вместе с состояниями настроения и тегами
### Пример
```http request
GET http://80.242.58.161:8082:8082/api/user/full
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJJdmFuIiwiZXhwIjoxNzE3MDU1OTY4LCJ1c2VySWQiOiIxIiwiZGV2IjoiTVlfREVWSUNFIn0.nCFLz-Z1V9OdkiVHUcKW81q4UsW8jmA1CHme9nnM6A3zG7G0gEUuiuoaKPvTgzqx-l7c62EmK_FIfGmnQNwtAA
```

### Успешный ответ
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

## 2. Сохранение нового пользователя
Так как сохранение нового пользователя связано с системой безопасности, то сохранение производится [с ее помощью](#управление-учетной-записью-пользователя)

## 3. Изменение пользователя
С помощью этого метода изменению подлежат все пользовательские поля, кроме email, password. Для сброса пароля нужно использовать [специальный метод](#5-сброс-пароля). Так же, как и для [изменения почты](#6-изменение-почты).
Данные состояний настроения и тегов нужно изменять так же с помощью сторонних методов.

### Пример
Изменение _username_:
```http request
PUT http://80.242.58.161:8082:8082/api/user
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJJdmFuIiwiZXhwIjoxNzE3MDYzNDc1LCJkZXYiOiJNWV9ERVZJQ0UiLCJ1c2VySWQiOiIxIn0.L0KHWJ2cepwUvDFbiZfX0-Z9vDD-Lo7PTqWDTpXhwGohsjr4_guZ1y99g-IgCOtilfEjtg4N_SImenQnDaqoUQ

{
  "username": "Ivan"
}
```

### Успешный ответ
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

### Ошибки
Пользователь отправил некорректные данные для изменения. Например, username уже существует:
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

## 4. Удаление пользователя
### Пример
```http request
DELETE http://80.242.58.161:8082:8082/api/user
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJJdmFuIiwiZXhwIjoxNzE3MDYzNDc1LCJkZXYiOiJNWV9ERVZJQ0UiLCJ1c2VySWQiOiIxIn0.L0KHWJ2cepwUvDFbiZfX0-Z9vDD-Lo7PTqWDTpXhwGohsjr4_guZ1y99g-IgCOtilfEjtg4N_SImenQnDaqoUQ
```

### Успешный ответ
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
## 1. Получение тега настроения
### Пример
```http request
GET http://80.242.58.161:8082:8082/api/mood_tag
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJJdmFuIiwiZXhwIjoxNzE3MDY1MzUzLCJkZXYiOiJNWV9ERVZJQ0UiLCJ1c2VySWQiOiI0In0.93im0syhNWJ8OOxUasa5MWqjfiTnIMkQkBnVfGV8CJJa87JSI18ZniWUasuZQp64HqAM6aj8KBgseT4TBjdpXw
```

### Успешный ответ
В ответе передается список всех тегов настроения пользователя:
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

Если теги еще не были добавлены:
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

## 2. Добавление нового тега настроения
### Пример
```http request
### POST Request
POST http://80.242.58.161:8082:8082/api/mood_tag
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJJdmFuIiwiZXhwIjoxNzE3MDY1MzUzLCJkZXYiOiJNWV9ERVZJQ0UiLCJ1c2VySWQiOiI0In0.93im0syhNWJ8OOxUasa5MWqjfiTnIMkQkBnVfGV8CJJa87JSI18ZniWUasuZQp64HqAM6aj8KBgseT4TBjdpXw

{
  "name": "girl friend",
  "degree": 3
}
```

### Успешный ответ
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

### Ошибки
Повторное добавление тега с тем же именем и _user_id_:
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
_user_id_ определяется по Bearer токену, который передается вместе с запросом

## 3. Изменение определенного тега настроения
### Пример
Изменение степени настроения в теге с 3 до 4
```http request
PUT http://80.242.58.161:8082:8082/api/mood_tag
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJJdmFuIiwiZXhwIjoxNzE3MDY1MzUzLCJkZXYiOiJNWV9ERVZJQ0UiLCJ1c2VySWQiOiI0In0.93im0syhNWJ8OOxUasa5MWqjfiTnIMkQkBnVfGV8CJJa87JSI18ZniWUasuZQp64HqAM6aj8KBgseT4TBjdpXw

{
  "id": 1,
  "name": "girl friend",
  "degree": 4
}
```

### Успешный ответ
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

Был указан неверный id тега при изменении данных: 
### Ошибки
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

## 4. Удаление тега настроения
### Пример
```http request
DELETE http://80.242.58.161:8082:8082/api/mood_tag?id=1
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJJdmFuIiwiZXhwIjoxNzE3MDY3MjYzLCJkZXYiOiJNWV9ERVZJQ0UiLCJ1c2VySWQiOiIxIn0.HE13E5tbLktTnYq7LGPWJNFwPJNuAcoR-f1BL1hWpWSDPrRTXBOXM8gZA7HrIteDFWwtV4SKyK2e0dTmtNiXlw
```

### Успешный ответ
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

### Ошибки
Указан id несуществующего тега:
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
## 1. Получение состояния настроения
### Пример
```http request
GET http://80.242.58.161:8082:8082/api/mood_entry?start_date=2024-04-01&end_date=2024-04-18
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJJdmFuIiwiZXhwIjoxNzE3MDY3MjYzLCJkZXYiOiJNWV9ERVZJQ0UiLCJ1c2VySWQiOiIxIn0.HE13E5tbLktTnYq7LGPWJNFwPJNuAcoR-f1BL1hWpWSDPrRTXBOXM8gZA7HrIteDFWwtV4SKyK2e0dTmtNiXlw
```

### Успешные ответы
По выбранному диапазону дат не найдены состояния настроения: 
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
Данный метод возвращает успещный ответ со статусом: NO_CONTENT

Состояния настроения по указаному диапазону найдены:
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

### Ошибки

## 3. Сохранение нового состояния настроения
Во время сохранения состояния настроения можно так же добавить новые теги или изменить старые. Для добавления новых тегов достаточно просто указать данные, которые пользователь хочет сохранить. Для изменения старых, нужно указать id тега и новые данные
### Пример
```http request
POST http://80.242.58.161:8082:8082/api/mood_entry
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

### Успешный ответ
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

### Ошибки
Попытка добавить тег с названием, который уже существует. Или изменить тег, название которого конфликтует с другими:
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

## 3. Изменение тега настроения
### Пример
Для того чтобы изменить тег настроения, нужно указать его id и данные, которые нужно изменить. 
Для изменения тегов нужно так же указать их id. Если необходимо добавить новый тег, то нужно указать данные для него без id
```http request
PUT http://80.242.58.161:8082:8082/api/mood_entry
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

### Успешный ответ
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
Метод возвращает успешный статус: NO_CONTENT

### Ошибки
При попытке создать или изменить имя тега на то, которое уже существует у пользователя:
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

## 4. Удаление тега настроения
### Пример
```http request
DELETE http://80.242.58.161:8082:8082/api/mood_entry?id=1
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJJdmFuIiwiZXhwIjoxNzE3MDY3MjYzLCJkZXYiOiJNWV9ERVZJQ0UiLCJ1c2VySWQiOiIxIn0.HE13E5tbLktTnYq7LGPWJNFwPJNuAcoR-f1BL1hWpWSDPrRTXBOXM8gZA7HrIteDFWwtV4SKyK2e0dTmtNiXlw
```

### Успешный ответ
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
Метод возвращает успешный статус: NO_CONTENT

### Ошибки
При указании несуществующего _id_ состояния настроения:
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

### Дополнительные ссылки на документацию
Для получения дополнительной информации, пожалуйста, ознакомьтесь со следующими разделами:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.1.5/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.1.5/maven-plugin/reference/html/#build-image)