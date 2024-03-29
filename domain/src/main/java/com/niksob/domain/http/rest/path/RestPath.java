package com.niksob.domain.http.rest.path;

import com.niksob.domain.config.properties.DatabaseConnectionProperties;

import java.util.Map;

public interface RestPath {
    String getWithParams(DatabaseConnectionProperties restProperties, String resourceUri, Map<String, String> params);

    String getWithBody(DatabaseConnectionProperties restProperties, String resourceUri);
}
