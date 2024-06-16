# My Mood Tracker Server Guide

## Overview
This application serves as the backend for the mobile app _Good Mood_, a mental health tracker. Users log their mood states multiple times a day, rating them and tagging influences, such as family or friends. The server's tasks include user registration, authentication, data storage, and serving as an interface for data operations.

**Description of the REST methods of the server:** _gateway-service/README-en.md_

## Microservices
My Mood Tracker Server is composed of several microservices that together fulfill the application's requirements.

### 1. Config-service
This microservice gathers all common configuration files from the _common-config_ directories in each application's resources and shares them with other microservices.

In production mode, it operates through GitHub. The application loads all configuration files from a remote repository. It then uses a special configuration file (specified by the variable `repo.remote.git.properties-file`) to compile these configurations into files for the microservices and serves them upon request.

For detailed usage instructions, refer to the user guide: **_config-service/README.md_**

### 2. Database-service
This microservice provides a REST API for interacting with database entities. Users can interact with the data through GET, POST, PUT, or DELETE methods.

**Dependencies:**
Several dependencies are required for this microservice to function. They must be running on a specific port and accessible to the database-service.

- **Config-service** - provides configurations for the microservice startup.
- **MySQL** - the database where all data is stored.
- **Redis** - used for caching. All entities loaded into the main database are cached using this service.

### 3. Mail-sender
This service sends emails to users. It is used for email verification by sending an activation code link during registration or email change.

**Dependencies:**

- **Config-service** - provides configurations for the microservice startup.
- **imap.yandex.ru** - Yandex servers used for sending messages via IMAP.

For detailed usage instructions, refer to the user guide: **_mail-sender/README.md_**

### 4. Authorization-service
This microservice handles user registration, authorization, and authentication.
Registration occurs via a request with an email and password. The user must confirm their email using the activation code sent to them.
Authorization and authentication are managed via an access token provided in the Bearer header.
The access token can be refreshed using a refresh token, which is generated along with the access token.

**Dependencies:**

- **Config-service** - provides configurations for the microservice startup.
- **Database-service** - used for accessing user data: it verifies user existence and loads new data.
- **Redis** - used for caching authorization-service data. Before new user data is added to the database through the database-service, it is cached along with the activation code in Redis, waiting for email confirmation.
- **Mail-sender** - used for sending activation code emails to new users.

### 5. Gateway-service
This microservice serves as the entry point to the entire application. It functions as a request router between microservices.
It also secures traffic routes to prevent unauthorized access to other microservices or sensitive data.

**Dependencies:**

- **Config-service** - provides configurations for the microservice startup.
- **Authorization-service** - handles registration, authorization, and authentication before accessing data.
- **Database-service** - provides data from the database.

## Other Application Components
### 1. Domain
This module contains business models, DTOs, converters, and exception classes supporting the application.

### 2. Logger
This module extends the standard Spring logger, Logback, with additional classes.
`ObjectStateLogger` adds the state of an object to the log message. Application settings allow specifying which fields of logged objects should be masked (e.g., passwords, tokens, etc.).
For detailed functionality, refer to the guide: **_logger/README.md_**

### 3. Layer-connector
This module supports an annotation processor that implements automatic model mapping between different application layers.
For detailed functionality, refer to the guide: **_layer-connector/README.md_**

### Additional Documentation Links

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.1.5/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.1.5/maven-plugin/reference/html/#build-image)
