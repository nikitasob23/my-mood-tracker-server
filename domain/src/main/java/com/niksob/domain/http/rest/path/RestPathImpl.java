package com.niksob.domain.http.rest.path;

import com.niksob.domain.config.properties.ConnectionProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RestPathImpl implements RestPath {
    @Override
    public String getWithParams(ConnectionProperties connectionProperties, String resourceUri, Map<String, String> params) {
        final String uri = connectionProperties.getProtocol() +
                "://" + connectionProperties.getHostname() +
                ":" + connectionProperties.getPort() +
                connectionProperties.getPath() +
                resourceUri;
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
        return connectionProperties.getProtocol() +
                "://" + connectionProperties.getHostname() +
                ":" + connectionProperties.getPort() +
                connectionProperties.getPath() +
                resourceUri;
    }
}
