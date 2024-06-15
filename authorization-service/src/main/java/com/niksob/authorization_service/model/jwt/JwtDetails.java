package com.niksob.authorization_service.model.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtDetails {
    private final String username;
    private final String userId;
    private final String device;
}
