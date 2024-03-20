package com.niksob.database_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigurationProperties
@ComponentScan(basePackages = {
        "com.niksob.logger",
        "com.niksob.domain",
        "com.niksob.database_service"
})
public class DatabaseMicroservice {

    public static void main(String[] args) {
        SpringApplication.run(DatabaseMicroservice.class, args);
    }
}
