package com.niksob.domain.http.rest.path;

import com.niksob.domain.config.properties.ConnectionProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RestPathImpl implements RestPath {
    @Override
    public String getWithParams(ConnectionProperties connectionProperties, String resourceUri, Map<String, String> params) {
        final String uri = new StringBuilder(connectionProperties.getProtocol())
                .append("://").append(connectionProperties.getHostname())
                .append(":").append(connectionProperties.getPort())
                .append(connectionProperties.getPath())
                .append(resourceUri)
                .toString();
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
    public String getWithBody(ConnectionProperties connectionProperties, String resourceUri) {
        return new StringBuilder(connectionProperties.getProtocol())
                .append("://").append(connectionProperties.getHostname())
                .append(":").append(connectionProperties.getPort())
                .append(connectionProperties.getPath())
                .append(resourceUri)
                .toString();
    }
}
