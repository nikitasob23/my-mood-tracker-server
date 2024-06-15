package com.niksob.authorization_service.service.auth.activation.code;

import com.niksob.domain.model.auth.login.active_code.ActiveCode;
import com.niksob.domain.model.user.Email;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class ActiveCodeServiceImpl implements ActiveCodeService {
    @Override
    public ActiveCode generate(Email email) {
        return new ActiveCode(UUID.randomUUID().toString());
    }
}
