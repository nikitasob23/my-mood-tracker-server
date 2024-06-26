package com.niksob.authorization_service.repository.user.activation;

import com.niksob.domain.model.user.activation.ActivationUserDetails;
import com.niksob.domain.model.auth.login.active_code.ActiveCode;
import com.niksob.domain.model.user.User;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class TempActivationUserRepoImpl implements TempActivationUserRepo {
    public static final String USER_ACTIVATION_DETAILS_CACHE_NAME = "user_activation_details_cache";

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(TempActivationUserRepoImpl.class);

    @Override
    @Cacheable(value = USER_ACTIVATION_DETAILS_CACHE_NAME, key = "#activeCode.getValue()")
    public User load(ActiveCode activeCode) {
        log.info("Activation user details is not found in cache", activeCode);
        return null; //If active code was generate, cache will return it. If wasn't generate, then will return null
    }

    @Override
    @CachePut(value = USER_ACTIVATION_DETAILS_CACHE_NAME,
            key = "#activationUserDetails.getActiveCode().getValue()")
    public User save(ActivationUserDetails activationUserDetails) {
        log.info("Save activation user details in cache", activationUserDetails);
        return activationUserDetails.getUserDetails();
    }

    @Override
    @CacheEvict(value = USER_ACTIVATION_DETAILS_CACHE_NAME, key = "#activeCode.getValue()")
    public void remove(ActiveCode activeCode) {
    }
}
