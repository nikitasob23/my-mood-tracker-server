# Application Logger

This custom logger captures all messages in json format with the state of the object and masking of private fields of objects.

## Configuration file
The names of the fields to be masked should be placed in the logger-config.yml configuration file.

### Configuration Example:
```
logger:
  message:
    masked:
      field-names:
        - {field_name1}
        - {field_name2}
```

### Log Example:
```
2023-02-05 22:46:34,635 [http-nio-8081-exec-1] DEBUG  c.n.d.service.user.UserService - [objectState="UserInfo": {"password":"*****","nickname":{"value":"TEST_NICKNAME"},"username":{"value":"TEST_USERNAME"}}] - Failed saving user
java.lang.IllegalArgumentException: Unsafe password
	at com.niksob.database_service.config.service.user.UserServiceConfig$1.save(UserServiceConfig.java:49)
	...
```
_*****_ - masked password

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.1.5/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.1.5/maven-plugin/reference/html/#build-image)

