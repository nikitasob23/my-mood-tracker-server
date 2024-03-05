package com.niksob.domain.http.rest.path;

import com.niksob.domain.model.connection.properties.RestConnectionProperties;

import java.util.Map;

public interface RestPath {
    String get(RestConnectionProperties restProperties, String resourceUri, Map<String, String> params);

    String post(RestConnectionProperties restProperties, String resourceUri);
}
