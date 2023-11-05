package com.niksob.logger.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TestObjectUser {
    private TestUsername username;
    private TestPassword password;
    private TestRefreshToken refreshToken;
}
