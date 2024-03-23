package com.niksob.domain.model.auth.token.details;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class AuthTokenEntityDetails {
    @NonNull
    private final Long userId;
    @NonNull
    private final String device;
}
