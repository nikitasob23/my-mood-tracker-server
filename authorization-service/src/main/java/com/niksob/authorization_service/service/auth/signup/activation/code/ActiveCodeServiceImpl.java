package com.niksob.authorization_service.service.auth.signup.activation.code;

import com.niksob.domain.model.auth.login.active_code.ActiveCode;
import com.niksob.domain.model.user.Email;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ActiveCodeServiceImpl implements ActiveCodeService {
    private final PasswordEncoder encoder;

    @Override
    public ActiveCode generate(Email email) {
        final String encodedEmail = encoder.encode(email.getValue());
        return new ActiveCode(encodedEmail);
    }

    @Override
    public boolean matches(Email email, ActiveCode activeCode) {
        return encoder.matches(email.getValue(), activeCode.getValue());
    }
}
