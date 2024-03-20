package com.niksob.authorization_service.service.password_encoder.PasswordEncoderService;

import com.niksob.domain.model.user.Password;
import com.niksob.domain.model.user.RowPassword;

public interface PasswordEncoderService {
    Password encode(RowPassword rowPassword);

    boolean matches(RowPassword rowPassword, Password password);
}
