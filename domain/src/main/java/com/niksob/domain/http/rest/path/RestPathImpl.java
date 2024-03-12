package com.niksob.domain.http.rest.path;

import com.niksob.domain.path.microservice.MicroservicePorts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RestPathImpl implements RestPath {
    @Value("${server.protocol}")
    private String protocol;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${server.hostname}")
    private String hostname;

    @Override
    public String get(String resourceUri, Map<String, String> params) {
        final String uri = "%s://%s:%s%s/%s"
                .formatted(protocol, hostname, MicroservicePorts.DATABASE, contextPath, resourceUri);

        if (params == null || params.isEmpty()) {
            return uri;
        }
        final StringBuilder uriBuilder = new StringBuilder(uri).append("?");

        final String paramsForUri = params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .reduce((p1, p2) -> p1 + "&" + p2)
                .orElse("");
        return uriBuilder.append(paramsForUri).toString();
    }

    @Override
    public String post(String resourceUri) {
        return "%s://%s:%s%s/%s".formatted(protocol, hostname, MicroservicePorts.DATABASE, contextPath, resourceUri);
    }
}
