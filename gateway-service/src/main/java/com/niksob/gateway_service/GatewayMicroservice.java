package com.niksob.gateway_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.niksob.logger",
        "com.niksob.domain",
        "com.niksob.gateway_service"
})
public class GatewayMicroservice {

    public static void main(String[] args) {
        SpringApplication.run(GatewayMicroservice.class, args);
    }

}
