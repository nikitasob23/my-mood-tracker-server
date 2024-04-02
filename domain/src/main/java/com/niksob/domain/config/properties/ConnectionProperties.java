package com.niksob.domain.config.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public abstract class ConnectionProperties {
    protected String protocol;
    protected String hostname;
    protected String port;
    protected String path;
}
