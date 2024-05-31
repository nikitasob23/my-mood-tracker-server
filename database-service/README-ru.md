# Руководство API сервиса database-service
Open API v1.0.0

## Обзор
Данный микросервис предоставляет REST API для взаимодействия микросервисов приложения Mood tracker с базой данных.

## Содержание
1. [Зависимости](#зависимости)
2. [Основные сущности](#основные-сущности)
3. [REST методы](#rest-методы)
   - [User](#User)
   - [MoodTag](#MoodTag)
   - [MoodEntry](#MoodEntry)
4. [Документация](#дополнительные-ссылки-на-документацию)

## Зависимости
Проект database-service использует следующие Maven зависимости для обеспечения функциональности, управления базами данных, кэширования, логирования, и других ключевых аспектов сервиса:

### Spring boot
1. **Spring Boot Starter** - зависимость, обеспечивающая работу Spring
2. **Spring boot starter test** - библиотека для работы с тестами
3. **spring boot starter web** - предоставляет все необходимые инструменты для веб-функционала: RESTful контроллеров, сервлетов и тд 
4. **spring Web Flux** - фреймворк, использующийся для асинхронной и реактивной обработки веб-запросов
5. **Spring Data JPA** - обеспечивает работу с базой данных через автоматическое создание репозиториев и других абстракций
6. **Spring Cache** - обеспечивает кэширование данных из базы и других вспомогательных значений
7. **Spring Data Redis** - NoSQL хранилище для кэша приложения
8. **Spring cloud config client** - клиент, обеспечивающий получение конфигурации для микросервиса

### Логирование
1. **Logstash Logback Encoder** - библиотека для логирования данных в определенном формате
2. **Logger** - модуль, реализующий работу кастомного логера. Данный логер добавляет к сообщениям состояние объекта

### Прочее
1. **Lombok** - используется для уменьшения шаблонного кода
2. **MapStruct** - фреймворк для маппинга разных моделей и сущностей, уменьшая количество кода и потенциальных ошибок при преобразовании данных

### Работа с данными
1. **MySQL Connector J** — драйвер для подключения к базам данных MySQL.
2. **Flyway MySQL** — инструмент для версионирования и миграции баз данных.

### Вспомогательные модули
1. **Domain** - модуль, содержащий основные модели и вспомогательные компоненты для функционирования микросервиса.
2. **Layer connector** - модуль, который обеспечивает автоматический маппинг моделей между разными слоями приложения

## Конфигурация
### 1. Получение конфигурации
В микросервисе есть возможность получения конфигурации из config service, поэтому вы можете указать имя сервиса и адрес, по которому будет отправлен запрос на получение конфигурации
```
spring:
  application:
    name: [APP_NAME]
  config:
    import: configserver:[CONFIG_SERVER_ADDRESS]
```

### 2. Подключение кэша
Микросервис использует redis в качестве кэш хранилища, данные для подключения:
```yaml
spring:
  data:
    redis:
      host: [HOST]
      port: [PORT]
```
### 3. Подключение базы данных
```yaml
spring:
  datasource:
    url: jdbc:mysql://[HOST]:[PORT]/[DB_NAME]
    username: [USERNAME]
    password: [PASSWORD]
    driver-class-name: com.mysql.cj.jdbc.Driver
```
### 4. Логирование
Так как в проекте используется кастомный логер: ObjectStateLogger, который логгирует не только сообщение, но и состояние указанного объекта, важно указать фабрику для генерации логгера. А так же названия тех полей, которые логгер должен маскировать при логгировании состояния объекта
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
### 5. Данные для подключения
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
### 6. Другие настройки
Так как записей mood entries в базе данных может быть очень много, то их получение возможно только по диапазону дат. Для указания диапазона дат по умолчанию, нужно использовать данные настройки
```yaml
service:
  loading:
    mood-entry:
      def-date-interval-days: 15
```

## Основные сущности
### 1. User
Сущность, содержащая основные данные пользователя, включая данные авторизации и бизнес модели
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
Сущность, содержащая слепок состояния настроения в определенный момент времени. У него есть основной параметр - это степень настроения.  
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
Сущность, характеризующая состояния настроения. Это могут быть какие-то люди или обстоятельства, которые влияют на состояние. Например, друзья или работа. 
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

## REST методы
В продакшене API данного микросервиса рассчитан на взаимодействие только в рамках внутренних сетей. Микросервис не подразумевает авторизации или аутентификации. Этим занимается auth-service в рамках приложения. Сервис отдает и принимает данные только в формате JSON.
## User

### 1. Проверка существования пользователя по email
### Пример запроса

```http request
GET http://localhost:8082/api/user?username=Ivan
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJJdmFuIiwiZXhwIjoxNzE2ODk5MzgzLCJkZXYiOiJNWV9ERVZJQ0UiLCJ1c2VySWQiOiIzIn0.5r4HYBkec0NkbuZ6HdJE2gWfEM3P-J7uq3MNNhQo2EK6TxdKJaeNo8a1RFoCfqzUwRVhBC7B4qdDyqf5haeMQw
```

### Успешный ответ:
```
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
**Пользователь не найден:**
```
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 21 May 2024 20:01:02 GMT
Keep-Alive: timeout=60
Connection: keep-alive

false
```

## 2. Получение пользователя
### Пример запроса

GET http://80.242.58.161:8081/api/service/database/user?username=Ivan

### Успешный ответ
```
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

### Ошибки
Случай, когда пользователь не найден по _username_:
```
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

## 3. Получение полного пользователя с состояниями и тегами настроения

### Пример запроса
GET http://80.242.58.161:8081/api/service/database/user/full?username=Ivan

### Успешный ответ
```
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

### Ошибки
Пользователь не найден по _username_:
```
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

## 4. Добавление нового пользователя:

### Пример запроса
POST http://80.242.58.161:8081/api/service/database/user

**Body:**
```
Content-Type: application/json

{
  "email": "IvanIvanov@mail.com",
  "username": "Ivan",
  "password": "SECRET"
}
```

### Успешный ответ
```
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

### Ошибки
Пользователь уже существует:
```
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

Пользователь отправил не все данные:
```
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

## 5. Изменение пользователя:
### Пример запроса
PUT http://80.242.58.161:8081/api/service/database/user

**Body:**
```
Content-Type: application/json

{
  "id": 4,
  "email": "IvanIvanov@gmail.com",
  "username": "Ivan",
  "password": "TOTAL_SECRET"
}
```

### Успешный ответ

На данный запрос возвращается успешный статус: NO_CONTENT с пустым телом ответа 
```
HTTP/1.1 204 
Date: Tue, 21 May 2024 19:37:58 GMT
Keep-Alive: timeout=60
Connection: keep-alive
```

### Ошибки
Пользователь не найден:
```
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
Пользователь отправил не все данные:
```
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

## 6. Удаление пользователя:
### Пример запроса

DELETE http://80.242.58.161:8081/api/service/database/user?username=Ivan

### Успешный ответ
На данный запрос возвращается успешный статус: NO_CONTENT с пустым телом ответа 
```
HTTP/1.1 204 
Date: Tue, 21 May 2024 19:42:29 GMT
Keep-Alive: timeout=60
Connection: keep-alive
```

### Ошибки
```
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

### 1. Получение тега настроения
### Пример запроса
GET http://80.242.58.161:8081/api/service/database/mood_tag?user_id=9

### Успешный ответ
```
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

Если токена не существует, возвращается статус: NO_CONTENT
```
HTTP/1.1 204 
Content-Type: application/json
Date: Tue, 21 May 2024 20:53:09 GMT
Keep-Alive: timeout=60
Connection: keep-alive
```

### Ошибки
Если пользователь, которому принадлежит тег, не существует:
```
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

### 2. Добавление нового тега настроения
### Пример запроса
POST http://80.242.58.161:8081/api/service/database/mood_tag

**body:**
```
Content-Type: application/json

{
  "name": "moves",
  "degree": 4,
  "userId": 11
}
```

### Успешный ответ
```
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

### Ошибки
Тег уже существует:
```
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

Пользователя, к которому пытаются добавить тег, не существует:
```
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

### 3. Обновление тега настроения
### Пример запроса
PUT http://80.242.58.161:8081/api/service/database/mood_tag

**body:**
```
Content-Type: application/json

{
  "id": 8,
  "name": "friends",
  "degree": 3,
  "userId": 12
}
```

### Успешный ответ
В случае удачной попытки обновления тега, возвращается успешный статус: NO_CONTENT
```
HTTP/1.1 204 
Date: Tue, 21 May 2024 21:15:38 GMT
Keep-Alive: timeout=60
Connection: keep-alive
```

### Ошибки
Пользователя, тег которого пытаются удалить, не существует:
```
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

Тега не существует:
```
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

### 3. Удаление тега настроения
### Пример запроса
DELETE http://80.242.58.161:8081/api/service/database/mood_tag?id=8&user_id=12

### Успешный ответ
В случае удачной попытки обновления тега, возвращается успешный статус: NO_CONTENT
```
HTTP/1.1 204 
Date: Tue, 21 May 2024 21:23:22 GMT
Keep-Alive: timeout=60
Connection: keep-alive
```

### Ошибки
Пользователя, тег которого пытаются удалить, не существует:
```
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

Тега не существует:
```
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

### 1. Получение тега настроения
### Пример запроса
GET http://80.242.58.161:8081/api/service/database/mood_entry?user_id=4&start_date=2024-05-01&end_date=2024-05-23

### Успешный ответ
```
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

Если состояния не существует, возвращается статус: NO_CONTENT
```
HTTP/1.1 204 
Content-Type: application/json
Date: Tue, 21 May 2024 21:26:38 GMT
Keep-Alive: timeout=60
Connection: keep-alive
```

### Ошибки
Если пользователя, которому принадлежит состояние, не существует:
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

### 2. Добавление нового состояния настроения
### Пример запроса
POST http://80.242.58.161:8081/api/service/database/mood_entry

**body:**
```
Content-Type: application/json

{
  "degree": 1,
  "user_id": 12,
  "date_time": "2024-04-10T23:07:38"
}
```

### Успешный ответ
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

Так же с состоянием можно добавлять новый тег настроения или редактировать старый.
Если нужно прикрепить старый тег и обновить его, то нужно указать id тега в запросе вместе с остальными данными. 
Если нужно добавить новый тег, то достаточно указать только обязательные поля: _name_, _user id_ 

POST http://80.242.58.161:8081/api/service/database/mood_entry

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

### Успешный ответ
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

### Ошибки
Тег, который добавлен в состояние настроения, невозможно обновить, так как не удается его найти:
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

Попытка повторного добавления тега, который уже создан, через состояние настроения: 
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

Добавление состояния настроения, когда пользователь еще не создан:
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

### 3. Обновление состояния настроения
### Пример запроса
PUT http://80.242.58.161:8081/api/service/database/mood_entry

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

### Успешный ответ
В случае удачной попытки обновления тега, возвращается успешный статус: NO_CONTENT
```
HTTP/1.1 204 
Date: Tue, 21 May 2024 23:10:00 GMT
Keep-Alive: timeout=60
Connection: keep-alive
```

### Ошибки
Тег, который добавлен в состояние настроения, невозможно обновить, так как не удается его найти:
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

Попытка повторного добавления тега, который уже создан, через состояние настроения:
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

Добавление состояния настроения, когда пользователь еще не создан:
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

Добавление в состояние настроения одного пользователя тега другого пользователя:
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

### 3. Удаление состояния настроения
### Пример запроса
DELETE http://80.242.58.161:8081/api/service/database/mood_entry?id=9

### Успешный ответ
В случае удачной попытки обновления состояния настроения, возвращается успешный статус: NO_CONTENT
```
HTTP/1.1 204 
Date: Tue, 21 May 2024 23:16:41 GMT
Keep-Alive: timeout=60
Connection: keep-alive
```

### Ошибки
Состояния настроения не существует:
```
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

### Дополнительные ссылки на документацию
Для получения дополнительной информации, пожалуйста, ознакомьтесь со следующими разделами:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.1.5/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.1.5/maven-plugin/reference/html/#build-image)

