package com.niksob.authorization_service.service.auth.signup.activation.code;

import com.niksob.domain.model.auth.login.active_code.ActiveCode;
import com.niksob.domain.model.user.Email;

public interface ActiveCodeService {
    ActiveCode generate(Email email);

    boolean matches(Email email, ActiveCode activeCode);
}
