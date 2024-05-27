package com.niksob.authorization_service.service.auth;

import com.niksob.authorization_service.service.auth.resetter.SecureDetailsResetter;
import com.niksob.authorization_service.service.auth.signout.SignOutService;
import com.niksob.authorization_service.service.auth.signup.SignupService;
import com.niksob.domain.model.auth.login.UserEmail;
import com.niksob.domain.model.auth.login.UserPasswordPair;
import com.niksob.domain.model.user.*;
import com.niksob.domain.model.auth.login.active_code.ActiveCode;
import com.niksob.domain.model.auth.login.SignupDetails;
import com.niksob.domain.model.auth.login.SignOutDetails;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class AuthServiceFacadeImpl implements AuthServiceFacade {
    private final SignupService signupService;
    private final SecureDetailsResetter secureDetailsResetter;
    private final SignOutService signOutService;

    @Override
    public Mono<Void> signup(SignupDetails signupDetails) {
        return signupService.signup(signupDetails);
    }

    @Override
    public Mono<Void> resetEmail(UserEmail userEmail) {
        return secureDetailsResetter.resetEmail(userEmail);
    }

    @Override
    public Mono<Void> resetPassword(UserPasswordPair userPasswordPair) {
        return secureDetailsResetter.resetPassword(userPasswordPair);
    }


    @Override
    public Mono<Void> signupByActiveCode(ActiveCode activeCode) {
        return signupService.signupByActiveCode(activeCode);
    }

    @Override
    public Mono<Void> resetEmailByActiveCode(ActiveCode activeCode) {
        return secureDetailsResetter.resetEmailByActiveCode(activeCode);
    }

    @Override
    public Mono<Void> signOut(SignOutDetails signOutDetails) {
        return signOutService.signOut(signOutDetails);
    }

    @Override
    public Mono<Void> signOutAll(UserId userId) {
        return signOutService.signOutAll(userId);
    }
}
