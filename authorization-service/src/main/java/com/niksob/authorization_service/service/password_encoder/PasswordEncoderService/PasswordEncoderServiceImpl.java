package com.niksob.authorization_service.service.password_encoder.PasswordEncoderService;

import com.niksob.domain.model.user.Password;
import com.niksob.domain.model.user.RowPassword;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PasswordEncoderServiceImpl implements PasswordEncoderService {
    private final PasswordEncoder passwordEncoder;

    @Override
    public Password encode(RowPassword rowPassword) {
        var passwordValue = passwordEncoder.encode(rowPassword.getValue());
        return new Password(passwordValue);
    }
}
