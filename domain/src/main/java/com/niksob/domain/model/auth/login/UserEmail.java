package com.niksob.domain.model.auth.login;

import com.niksob.domain.model.user.Email;
import com.niksob.domain.model.user.UserId;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserEmail {
    private final UserId userId;
    private final Email email;
}
