package com.niksob.domain.model.auth.login;

import com.niksob.domain.model.user.Email;
import com.niksob.domain.model.user.RowPassword;
import com.niksob.domain.model.user.Username;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class SignupDetails {
    @NonNull
    private final Email email;
    private final Username username;
    @NonNull
    private final RowPassword rowPassword;
}
