# Руководство Mail-sender

## Обзор
Данное приложения обеспечивает отправку кода активации пользователям на электронную почту. Данное сообщение нужно для подтверждения почты при регистрации или ее смене.

## Зависимости
Проект database-service использует следующие Maven зависимости для обеспечения функциональности, управления базами данных, кэширования, логирования, и других ключевых аспектов сервиса:

### Spring boot
1. **Spring Boot Starter** - зависимость, обеспечивающая работу Spring
2. **Spring boot starter test** - библиотека для работы с тестами
3. **spring boot starter web** - предоставляет все необходимые инструменты для веб-функционала: RESTful контроллеров, сервлетов и тд
4. **Spring cloud config client** - клиент, обеспечивающий получение конфигурации для микросервиса
5. **Spring boot starter mail** - обеспечивает основную функцию приложения, а именно поддержку отправки сообщений на электронную почту  

### Прочее
1. **Domain** - модуль, содержащий основные модели и вспомогательные компоненты для функционирования микросервиса.
2. **Lombok** - используется для уменьшения шаблонного кода

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

### 2. Данные для подключения
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
### 3. Отправка сообщений
Настройки для работы с _Spring boot starter mail_
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

### 4. Генерация ссылки для отправки кода активации
Основная цель отправки письма - это ссылка с кодом активации. Ссылка фомируется из данных настроек:
```yaml
mail:
  sending:
    activation-code:
      confirmation:
        path:
          protocol: ${microservice.connection.gateway.protocol}
          hostname: 80.242.58.161
          port: ${microservice.connection.gateway.port}
          base-path: ${microservice.connection.gateway.path}
```


## REST методы

### 1. Отправка кода активации для регистрации
### Пример запроса
```http request
POST http://127.0.0.1:8081/api/service/mail_sender/active_code/signup
Content-Type: application/json

{
  "sender_username": "Ivan",
  "recipient_email": "IvanIvanov@mail.com",
  "active_code":  "TEST_ACTIVE_CODE"
}
```

### Успешный ответ:
```http request
HTTP/1.1 204 
Date: Fri, 31 May 2024 08:31:30 GMT
Keep-Alive: timeout=60
Connection: keep-alive
```
Метод возвращает успешный статус: NO_CONTENT

### 2. Отправка кода активации для подтверждения изменения электронной почты 
### Пример запроса
```http request
POST http://127.0.0.1:8081/api/service/mail_sender/active_code/reset/email
Content-Type: application/json

{
  "sender_username": "Ivan",
  "recipient_email": "IvanIvanov@mail.com",
  "active_code":  "TEST_ACTIVE_CODE"
}
```

### Успешный ответ:
```http request
HTTP/1.1 204 
Date: Fri, 31 May 2024 08:52:06 GMT
Keep-Alive: timeout=60
Connection: keep-alive
```
Метод возвращает успешный статус: NO_CONTENT


### Дополнительные ссылки на документацию
Для получения дополнительной информации, пожалуйста, ознакомьтесь со следующими разделами:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.1.5/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.1.5/maven-plugin/reference/html/#build-image)