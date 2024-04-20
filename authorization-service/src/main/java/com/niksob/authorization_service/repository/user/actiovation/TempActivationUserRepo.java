package com.niksob.authorization_service.repository.user.actiovation;

import com.niksob.authorization_service.model.login.user.activation.ActivationUserDetails;
import com.niksob.domain.model.auth.login.active_code.ActiveCode;

public interface TempActivationUserRepo {
    ActivationUserDetails load(ActiveCode activeCode);

    ActivationUserDetails save(ActivationUserDetails activationUserDetails);
}
