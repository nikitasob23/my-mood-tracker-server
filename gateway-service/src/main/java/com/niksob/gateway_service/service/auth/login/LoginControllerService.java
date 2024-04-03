package com.niksob.gateway_service.service.auth.login;

import com.niksob.domain.dto.auth.login.SignOutDetailsDto;
import com.niksob.domain.dto.user.UserIdDto;
import com.niksob.domain.dto.user.signup.SignupDetailsDto;
import com.niksob.domain.mapper.dto.auth.login.SignOutDetailsDtoMapper;
import com.niksob.domain.mapper.dto.auth.login.SignupDetailsDtoMapper;
import com.niksob.domain.mapper.dto.user.id.UserIdDtoMapper;
import com.niksob.layer_connector.annotation.LayerConnector;
import reactor.core.publisher.Mono;

@LayerConnector(source = LoginService.class, mapper = {
        SignupDetailsDtoMapper.class, SignOutDetailsDtoMapper.class, UserIdDtoMapper.class
})
public interface LoginControllerService {
    Mono<Void> signup(SignupDetailsDto signupDetailsDto);

    Mono<Void> signOut(SignOutDetailsDto signOutDetails);

    Mono<Void> signOutAll(UserIdDto userId);
}
