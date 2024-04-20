package com.niksob.authorization_service.repository.user.actiovation;

import com.niksob.authorization_service.model.login.user.activation.ActivationUserDetails;
import com.niksob.domain.model.auth.login.active_code.ActiveCode;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class TempActivationUserRepoImpl implements TempActivationUserRepo {
    public static final String USER_ACTIVATION_DETAILS_CACHE_NAME = "user_activation_details_cache";

    @Override
    @Cacheable(value = USER_ACTIVATION_DETAILS_CACHE_NAME, key = "#activeCode")
    public ActivationUserDetails load(ActiveCode activeCode) {
        return null; //If active code was generate, cache will return it. If wasn't generate, then will return null
    }

    @Override
    @CachePut(value = USER_ACTIVATION_DETAILS_CACHE_NAME,
            key = "#activationUserDetails.getActiveCode()")
    public ActivationUserDetails save(ActivationUserDetails activationUserDetails) {
        return activationUserDetails;
    }
}
