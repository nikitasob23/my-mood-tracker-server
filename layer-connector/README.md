
# Layer Connector Library HELP
## Overview
The Layer Connector library is a Java annotation processor designed to simplify the integration of multi-layer architectures in Spring Boot applications. 
It automates the creation of adapter classes that connect two layers of your application. This README provides a guide on effectively using the library.

## Table of Contents
1. [Introduction](#introduction) 
2. [Getting Started](#getting-started) 
3. [Annotations](#annotations) 
4. [Example Usage](#example-usage) 
5. [Generated Classes](#generated-classes) 
6. [Error Handling](#error-handling) 
7. [Versioning](#versioning) 
8. [Contributing](#contributing) 
9. [License](#license)

## Introduction
Layer Connector aims to simplify the interaction between different layers of your application, 
reducing boilerplate code and promoting cleaner architecture. 
It uses annotation processing to automatically generate adapter classes, eliminating the need to manually implement these connectors.

## Getting Started
To integrate Layer Connector into your project, follow these steps:

1. Add the Layer Connector library to your project dependencies.
2. Annotate your service interface with @LayerConnector, providing necessary details such as the source class and mappers for converting required fields.
3. Build your project to trigger the annotation processor, which will generate the corresponding adapter class.

## Annotations
### @LayerConnector
The primary annotation used to mark the service interface for processing. 
It takes parameters such as the source class and required mappers.

## Example Usage
Suppose you have a controller that handles a request for data. It receives a UsernameDto DTO object.

```java
@RestController
@RequiredArgsConstructor
public class UserController {
    // ... fields
    @GetMapping("/user")
    public Mono<UserInfoDto> load(@RequestParam("username") UsernameDto usernameDto) {
        return userControllerService.load(usernameDto);
    }
    // ... other methods
}
```

And there is a `UserService` that contains some logic for processing data before responding.
The service, in its operation, uses other models, particularly `Username`.
```java
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    // ... fields
    @Override
    public Mono<UserInfo> load(Username username) {
        return Mono.just(username)
                .map(userDao::load);
    }
    // ... other methods
}
```

A request arises to connect these two layers of the application. This is where **Layer Connector** comes into play.

Consider the `UserControllerService` interface annotated with `@LayerConnector`.
```java
@LayerConnector(source = UserService.class, mapper = {UsernameDtoMapper.class, UserInfoDtoMapper.class, UserDtoMonoMapper.class})
public interface UserControllerService {
    Mono<UserInfoDto> load(UsernameDto usernameDto);
    // ... other methods
}
```

## Mandatory conditions for creating Layer Connector
1. An interface marked with the @LayerConnector annotation must contain the same method names as the source class.
2. The interface and source class must have only one parameter in the method arguments.
3. Mappers must contain methods for converting all necessary models.

The annotation processor will generate the corresponding adapter class `UserControllerServiceLayerConnector`, 
which acts as an adapter connecting the service layer with the data layer.
```java
@javax.annotation.processing.Generated(
        value = "com.niksob.layer_connector.processor.LayerConnectorProcessor",
        date = "2024-01-18T01:03:06+0300",
        comments = "comments = version: 0.0.1-SNAPSHOT, compiler: javac, environment: Java 17"
)
@org.springframework.stereotype.Component
public class UserControllerServiceLayerConnector implements com.niksob.database_service.controller.user.UserControllerService {
    private final com.niksob.domain.mapper.user.UserDtoMonoMapper userDtoMonoMapper;
    private final com.niksob.domain.mapper.user.UserInfoDtoMapper userInfoDtoMapper;
    private final com.niksob.database_service.service.user.UserService source;

    @Override
    public reactor.core.publisher.Mono<com.niksob.domain.dto.user.UserInfoDto> load(com.niksob.domain.dto.user.UsernameDto value) {
        com.niksob.domain.model.user.Username mapped = usernameDtoMapper.fromDto(value);
        reactor.core.publisher.Mono<com.niksob.domain.model.user.UserInfo> result = source.load(mapped);
        reactor.core.publisher.Mono<com.niksob.domain.dto.user.UserInfoDto> mappedResult = userDtoMonoMapper.toDtoMono(result);
        return mappedResult;
    }
    // ... other methods
}
```

Now you can use `UserService` in `UserController`, accessing `UserControllerService` and avoiding explicit mapping.

## Generated Classes
Generated adapter classes, such as `UserControllerServiceLayerConnector`, 
encapsulate the logic needed to bridge two different layers of the application. 
These classes are automatically created by the annotation processor, reducing the effort of manual implementation.

## Error Handling
The library provides a set of default error-handling mechanisms for common exceptions in service methods. 
Customize error handling by providing your own implementation in the service interface.

### Example Exceptional Situations
1. Null values were passed as parameters to the `@LayerConnector` annotation.
2. The mapper lacks a required method for converting a parameter or return value of a method.
3. The mapper has multiple methods that return the same value, and Layer Connector cannot determine which one to use.
4. The `@LayerConnector` annotation was passed a `source` class that does not have the full list of methods specified in the annotated interface.

## Logging
Logging is organized by issuing warnings during the project build. 
Typically, messages contain information about the Element class, which serves as an abstraction over all used classes: 
LayerConnector, source, mappers.

## Versioning
This library follows semantic versioning. Check the `@Generated` annotation in the generated classes for details on the library version, 
compiler version, and environment information.

## Contributing
We welcome contributions from the community.

## License
This Layer Connector library is licensed under the MIT License. See the LICENSE file for details.