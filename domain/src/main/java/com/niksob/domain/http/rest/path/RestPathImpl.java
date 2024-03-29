package com.niksob.domain.http.rest.path;

import com.niksob.domain.config.properties.DatabaseConnectionProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RestPathImpl implements RestPath {
    @Override
    public String getWithParams(DatabaseConnectionProperties connectionProperties, String resourceUri, Map<String, String> params) {
        final String uri = "%s://%s:%s%s/%s".formatted(
                connectionProperties.getProtocol(),
                connectionProperties.getHostname(),
                connectionProperties.getPort(),
                connectionProperties.getPath(),
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
    public String getWithBody(DatabaseConnectionProperties connectionProperties, String resourceUri) {
        return "%s://%s:%s%s/%s".formatted(
                connectionProperties.getProtocol(),
                connectionProperties.getHostname(),
                connectionProperties.getPort(),
                connectionProperties.getPath(),
                resourceUri
        );
    }
}
