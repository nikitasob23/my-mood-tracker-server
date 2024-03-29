package com.niksob.domain.model.auth.login;

import com.niksob.domain.model.user.UserId;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignOutDetails {
    private final UserId userId;
    private final String device;
}
