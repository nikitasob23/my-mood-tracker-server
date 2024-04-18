package com.niksob.mailsender.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.stream.Stream;

@Configuration
public class MailConfig {
    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.protocol}")
    private String protocol;

    @Value("${spring.mail.debug}")
    private String debug;


    @Bean
    public JavaMailSender getJavaMailSender() {
        return Stream.of(new JavaMailSenderImpl())
                .peek(m -> m.setHost(host))
                .peek(m -> m.setPort(port))
                .peek(m -> m.setUsername(username))
                .peek(m -> m.setPassword(password))
                .peek(this::setProperty) //set protocol and debug to JavaMailProperties
                .findFirst().get();
    }

    private void setProperty(JavaMailSenderImpl mailSender) {
        Stream.of(mailSender.getJavaMailProperties())
                .peek(p -> p.setProperty("mail.transport.protocol", protocol))
                .forEach(p -> p.setProperty("mail.debug", debug));
    }
}
