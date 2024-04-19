package com.niksob.authorization_service.values.user;

import com.niksob.domain.model.auth.login.SignupDetails;
import com.niksob.domain.model.user.Username;

public class DefaultUserInfo {
    public static Username createUsernameIfEmpty(SignupDetails signupDetails) {
        if (signupDetails.getUsername() == null || signupDetails.getUsername().getValue() == null) {
            return new Username(signupDetails.getEmail().getValue());
        }
        return signupDetails.getUsername();
    }
}
