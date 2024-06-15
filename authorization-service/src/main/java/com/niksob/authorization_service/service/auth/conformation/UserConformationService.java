package com.niksob.authorization_service.service.auth.conformation;

import com.niksob.domain.model.auth.login.SignupDetails;
import com.niksob.domain.model.auth.login.active_code.ActiveCode;
import com.niksob.domain.model.user.UserInfo;
import reactor.core.publisher.Mono;

public interface UserConformationService {
    Mono<Void> sendSignupActiveCodeMessage(SignupDetails signupDetails);

    Mono<Void> sendEmailResettingActiveCodeMessage(UserInfo userInfo);

    UserInfo getUserInfo(ActiveCode activeCode);
}
