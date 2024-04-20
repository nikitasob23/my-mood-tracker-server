package com.niksob.authorization_service.repository.user.actiovation;

import com.niksob.domain.model.user.activation.ActivationUserDetails;
import com.niksob.domain.model.auth.login.active_code.ActiveCode;
import com.niksob.domain.model.user.SecurityUserDetails;

public interface TempActivationUserRepo {
    SecurityUserDetails load(ActiveCode activeCode);

    SecurityUserDetails save(ActivationUserDetails activationUserDetails);
}
