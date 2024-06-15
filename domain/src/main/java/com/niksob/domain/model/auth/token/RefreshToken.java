package com.niksob.domain.model.auth.token;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RefreshToken {
    private final String value;
}
