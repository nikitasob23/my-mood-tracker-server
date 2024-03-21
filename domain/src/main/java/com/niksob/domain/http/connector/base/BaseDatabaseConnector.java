package com.niksob.domain.http.connector.base;

import com.niksob.domain.config.properties.DatabaseConnectionProperties;
import com.niksob.domain.http.client.HttpClient;
import com.niksob.domain.http.rest.path.RestPath;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BaseDatabaseConnector {
    protected final HttpClient httpClient;
    protected final RestPath restPath;
    protected final DatabaseConnectionProperties connectionProperties;
}
