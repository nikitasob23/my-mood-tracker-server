package com.niksob.authorization_service.service.auth.email;

import com.niksob.domain.model.user.Email;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service
public class EmailValidationService {
    private static final String EMAIL_REGEX =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private final Pattern pattern = Pattern.compile(EMAIL_REGEX);

    public boolean validate(Email email) {
        if (email == null || email.getValue() == null) {
            return false;
        }
        final Matcher matcher = pattern.matcher(email.getValue());
        return matcher.matches();
    }
}
