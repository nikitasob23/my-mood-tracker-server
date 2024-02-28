package com.niksob.domain.http.rest.path;

import com.niksob.domain.path.microservice.MicroservicePorts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RestPathImpl implements RestPath {
    @Value("${server.protocol}")
    private String protocol;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${server.hostname}")
    private String hostname;

    @Override
    public String get(String resourceUri) {
        return "%s://%s:%s%s/%s".formatted(protocol, hostname, MicroservicePorts.DATABASE, contextPath, resourceUri);
    }
}
