package com.niksob.domain.http.rest.path;

import com.niksob.domain.model.connection.properties.RestConnectionProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RestPathImpl implements RestPath {
    @Override
    public String get(RestConnectionProperties connectionProperties, String resourceUri, Map<String, String> params) {
        final String uri = "%s://%s:%s%s/%s".formatted(
                connectionProperties.getProtocol(),
                connectionProperties.getHostname(),
                connectionProperties.getPort(),
                connectionProperties.getContextPath(),
                resourceUri
        );
        if (params == null || params.isEmpty()) {
            return uri;
        }
        final String paramsForUri = params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .reduce((p1, p2) -> p1 + "&" + p2)
                .orElse("");

        return uri + "?" + paramsForUri;
    }

    @Override
    public String post(RestConnectionProperties connectionProperties, String resourceUri) {
        return "%s://%s:%s%s/%s".formatted(
                connectionProperties.getProtocol(),
                connectionProperties.getHostname(),
                connectionProperties.getPort(),
                connectionProperties.getContextPath(),
                resourceUri
        );
    }
}
