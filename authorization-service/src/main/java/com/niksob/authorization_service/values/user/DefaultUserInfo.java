package com.niksob.authorization_service.values.user;

import com.niksob.domain.model.auth.login.SignupDetails;
import com.niksob.domain.model.user.Username;

public class DefaultUserInfo {
    private static final String EMAIL_SPLIT_SYM = "@";

    public static Username createUsernameIfEmpty(SignupDetails signupDetails) {
        if (signupDetails.getUsername() != null && signupDetails.getUsername().getValue() != null) {
            return signupDetails.getUsername();
        }
        final String emailValue = signupDetails.getEmail().getValue();
        final String usernameValue = emailValue.split(EMAIL_SPLIT_SYM)[0];
        return new Username(usernameValue);
    }
}
