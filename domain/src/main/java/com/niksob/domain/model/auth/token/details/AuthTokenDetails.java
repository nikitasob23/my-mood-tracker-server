package com.niksob.domain.model.auth.token.details;

import com.niksob.domain.model.user.UserId;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthTokenDetails {
    private final UserId userId;
    private final String device;
}
