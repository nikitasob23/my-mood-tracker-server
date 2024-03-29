package com.niksob.authorization_service.controller.auth.signup;

import com.niksob.authorization_service.mapper.dto.signup.SignupDetailsDtoMapper;
import com.niksob.authorization_service.service.auth.signup.UserSignupService;
import com.niksob.domain.dto.user.UserIdDto;
import com.niksob.domain.dto.user.signup.SignupDetailsDto;
import com.niksob.domain.dto.auth.login.SignOutDetailsDto;
import com.niksob.domain.mapper.dto.auth.login.SignOutDetailsDtoMapper;
import com.niksob.domain.mapper.dto.user.id.UserIdDtoMapper;
import com.niksob.layer_connector.annotation.LayerConnector;
import reactor.core.publisher.Mono;

@LayerConnector(source = UserSignupService.class,
        mapper = {SignupDetailsDtoMapper.class, SignOutDetailsDtoMapper.class, UserIdDtoMapper.class})
public interface LoginControllerService {
    Mono<Void> signup(SignupDetailsDto userSignupDetails);

    Mono<Void> signOut(SignOutDetailsDto signOutDetails);

    Mono<Void> signOutAll(UserIdDto userId);
}
