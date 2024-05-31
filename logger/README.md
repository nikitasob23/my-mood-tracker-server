# Logger Usage Guide

## Overview
The Logger module is an add-on to the Logback logger and presents its Logger and LoggerFactory interfaces: ObjectStateLogger and ObjectStateLoggerFactory, respectively. This implementation allows you to log the state of an object separately in JSON format using a special format and MDC. It also masks sensitive data in logs.

## Application
Using ObjectStateLogger is especially convenient when logs are collected and displayed by other systems. With such a logger, you can separately collect the state of an object and monitor its changes. This approach is more visual.

## Getting started
To integrate Logger into your project, follow these steps:

### 1. Add a dependency on logger to your project
_For Maven:_
```
<dependency>
    <groupId>com.niksob</groupId>
    <artifactId>logger</artifactId>
    <version>1.0.0</version>
</dependency>
```
_For gradle:_
```
implementation 'com.niksob:logger:1.0.0'
```
### 2. Add the factory as a standard logger in the configuration file
   _For application.yml_:
```yaml
org:
  slf4j:
    LoggerFactory: com.niksob.logger.object_state.factory.ObjectStateLoggerFactory
```

_For application.properties:_
```properties
org.slf4j.LoggerFactory=com.niksob.logger.object_state.factory.ObjectStateLoggerFactory
```

### 3. Add the fields that need to be masked
_For application.yml_:
```yaml
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

_For application.properties:_
```properties
logger.message.masked.field-names=password,rowPassword,accessToken,access,refreshToken,refresh
```

## Example of the work
```java
public class UserRepository {
    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(CachedUserEntityLoaderDao.class);
    // Other fields
    public UserEntity loadById(Long id) {
        try {
            // Other code
        } catch (Exception e) {
            log.error("User entity not loaded from repository", e, user);
        }
        // Other code
    }
}
```
In this code, we log the state of the user object as a separate field in the logs, rather than integrating it into the message itself.

## License
This Layer Connector library is licensed under the MIT license. See the LICENSE file for more information.