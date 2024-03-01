package com.niksob.authorization_service.model.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtDetails {
    private final String subject;
    private final JwtClaims claims = new JwtClaims();
}
