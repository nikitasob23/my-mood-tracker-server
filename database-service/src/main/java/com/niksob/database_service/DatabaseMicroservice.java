package com.niksob.database_service;

import com.ulisesbocchio.jasyptspringboot.environment.StandardEncryptableEnvironment;
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
        SpringApplication application = new SpringApplication(DatabaseMicroservice.class);
        application.setEnvironment(new StandardEncryptableEnvironment());
        application.run(args);
    }
}
