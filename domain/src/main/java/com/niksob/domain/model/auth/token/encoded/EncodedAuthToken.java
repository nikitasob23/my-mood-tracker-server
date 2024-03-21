package com.niksob.domain.model.auth.token.encoded;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EncodedAuthToken {
    private final EncodedAccessToken access;
    private final EncodedRefreshToken refresh;
}
