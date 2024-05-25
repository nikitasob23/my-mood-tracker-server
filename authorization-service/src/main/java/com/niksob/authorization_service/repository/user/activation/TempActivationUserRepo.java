package com.niksob.authorization_service.repository.user.activation;

import com.niksob.domain.model.user.User;
import com.niksob.domain.model.user.activation.ActivationUserDetails;
import com.niksob.domain.model.auth.login.active_code.ActiveCode;

public interface TempActivationUserRepo {
    User load(ActiveCode activeCode);

    User save(ActivationUserDetails activationUserDetails);
}
