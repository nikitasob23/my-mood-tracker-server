package com.niksob.authorization_service.model.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Jwt {
    private final String value;
}
