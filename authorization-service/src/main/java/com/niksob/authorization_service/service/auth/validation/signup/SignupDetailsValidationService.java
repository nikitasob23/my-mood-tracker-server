package com.niksob.authorization_service.service.auth.validation.signup;

import com.niksob.domain.model.auth.login.SignupDetails;
import com.niksob.domain.model.user.Email;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.niksob.domain.model.user.RowPassword;
import org.springframework.stereotype.Service;

@Service
public class SignupDetailsValidationService {
    private static final String EMAIL_REGEX =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*\\d).{8,}$";

    private final Pattern emailPattern = Pattern.compile(EMAIL_REGEX);
    private final Pattern passwordPattern = Pattern.compile(PASSWORD_REGEX);

    public boolean validate(SignupDetails signupDetails) {
        return validate(signupDetails.getEmail()) && validate(signupDetails.getRowPassword());
    }

    public boolean validate(Email email) {
        if (email == null || email.getValue() == null) {
            return false;
        }
        final Matcher matcher = emailPattern.matcher(email.getValue());
        return matcher.matches();
    }

    public boolean validate(RowPassword password) {
        if (password == null || password.getValue() == null) {
            return false;
        }
        final Matcher matcher = passwordPattern.matcher(password.getValue());
        return matcher.matches();
    }
}
