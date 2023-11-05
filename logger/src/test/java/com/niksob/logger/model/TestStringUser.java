package com.niksob.logger.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TestStringUser {
    private final String username;
    private final String password;
    private final String refreshToken;
}
