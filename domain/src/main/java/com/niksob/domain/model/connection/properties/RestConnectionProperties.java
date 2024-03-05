package com.niksob.domain.model.connection.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class RestConnectionProperties {
    @NonNull
    private final String protocol;
    @NonNull
    private final String hostname;
    @NonNull
    private final String port;
    @NonNull
    private final String contextPath;
}

