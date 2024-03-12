package com.niksob.authorization_service.model.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class JwtClaims {
    private final Map<String, Object> storage;

    public JwtClaims() {
        this.storage = Map.of();
    }
}
