package com.niksob.domain.http.connector.base;

import com.niksob.domain.config.properties.ConnectionProperties;
import com.niksob.domain.http.client.HttpClient;
import com.niksob.domain.http.rest.path.RestPath;
import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public abstract class BaseConnector {
    protected final HttpClient httpClient;
    protected final RestPath restPath;
    protected final ConnectionProperties connectionProperties;

    protected String getWithParams(String uri, Map<String, String> params) {
        return restPath.getWithParams(connectionProperties, uri, params);
    }

    protected String getWithBody(String uri) {
        return restPath.getWithBody(connectionProperties, uri);
    }
}
