package com.niksob.gateway_service.model.user.security.role;

import org.springframework.security.core.GrantedAuthority;

public enum SecurityRole implements GrantedAuthority {

    USER;

    @Override
    public String getAuthority() {
        return name();
    }
}
