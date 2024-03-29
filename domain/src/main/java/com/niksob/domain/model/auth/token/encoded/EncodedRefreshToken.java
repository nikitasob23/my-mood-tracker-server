package com.niksob.domain.model.auth.token.encoded;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EncodedRefreshToken {
    private final String value;
}
