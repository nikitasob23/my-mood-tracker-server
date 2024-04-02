package com.niksob.domain.http.rest.path;

import com.niksob.domain.config.properties.ConnectionProperties;

import java.util.Map;

public interface RestPath {
    String getWithParams(ConnectionProperties restProperties, String resourceUri, Map<String, String> params);

    String getWithBody(ConnectionProperties restProperties, String resourceUri);
}
