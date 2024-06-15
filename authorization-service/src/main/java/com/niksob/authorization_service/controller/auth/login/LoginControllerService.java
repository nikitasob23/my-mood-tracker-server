package com.niksob.authorization_service.controller.auth.login;

import com.niksob.authorization_service.service.auth.AuthServiceFacade;
import com.niksob.domain.dto.auth.login.UserEmailDto;
import com.niksob.domain.dto.auth.login.UserPasswordPairDto;
import com.niksob.domain.dto.auth.login.active_code.ActiveCodeDto;
import com.niksob.domain.mapper.dto.auth.login.SignupDetailsDtoMapper;
import com.niksob.domain.dto.user.UserIdDto;
import com.niksob.domain.dto.user.signup.SignupDetailsDto;
import com.niksob.domain.dto.auth.login.SignOutDetailsDto;
import com.niksob.domain.mapper.dto.auth.login.SignOutDetailsDtoMapper;
import com.niksob.domain.mapper.dto.auth.login.UserEmailDtoMapper;
import com.niksob.domain.mapper.dto.auth.login.UserPasswordPairDtoMapper;
import com.niksob.domain.mapper.dto.auth.login.active_code.ActiveCodeDtoMapper;
import com.niksob.domain.mapper.dto.user.id.UserIdDtoMapper;
import com.niksob.layer_connector.annotation.LayerConnector;
import reactor.core.publisher.Mono;

@LayerConnector(source = AuthServiceFacade.class,
        mapper = {
                SignupDetailsDtoMapper.class,
                SignOutDetailsDtoMapper.class,
                UserIdDtoMapper.class,
                ActiveCodeDtoMapper.class,
                UserPasswordPairDtoMapper.class,
                UserEmailDtoMapper.class
        })
public interface LoginControllerService {
    Mono<Void> signup(SignupDetailsDto userSignupDetails);

    Mono<Void> signupByActiveCode(ActiveCodeDto activeCode);

    Mono<Void> signOut(SignOutDetailsDto signOutDetails);

    Mono<Void> signOutAll(UserIdDto userId);

    Mono<Void> resetPassword(UserPasswordPairDto passwordPair);

    Mono<Void> resetEmail(UserEmailDto userEmail);

    Mono<Void> resetEmailByActiveCode(ActiveCodeDto activeCode);
}
