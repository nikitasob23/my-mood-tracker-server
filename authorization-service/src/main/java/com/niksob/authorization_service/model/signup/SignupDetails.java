package com.niksob.authorization_service.model.signup;

import com.niksob.domain.model.user.RowPassword;
import com.niksob.domain.model.user.Username;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignupDetails {
    private final Username username;
    private final RowPassword rowPassword;
}
