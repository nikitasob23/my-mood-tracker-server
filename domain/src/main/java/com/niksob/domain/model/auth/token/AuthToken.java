package com.niksob.domain.model.auth.token;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthToken {
    private final AccessToken access;
    private final RefreshToken refresh;
}
