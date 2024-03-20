package com.niksob.authorization_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.niksob.logger",
        "com.niksob.domain",
        "com.niksob.authorization_service"
})
public class AuthorizationMicroservice {
    public static void main(String[] args) {
        SpringApplication.run(AuthorizationMicroservice.class, args);
    }
}
