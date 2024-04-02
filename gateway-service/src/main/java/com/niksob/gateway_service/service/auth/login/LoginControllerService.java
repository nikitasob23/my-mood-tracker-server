package com.niksob.gateway_service.service.auth.login;

import com.niksob.domain.dto.user.signup.SignupDetailsDto;
import com.niksob.domain.mapper.dto.auth.login.SignupDetailsDtoMapper;
import com.niksob.layer_connector.annotation.LayerConnector;
import reactor.core.publisher.Mono;

@LayerConnector(source = LoginService.class, mapper = SignupDetailsDtoMapper.class)
public interface LoginControllerService {
    Mono<Void> signup(SignupDetailsDto signupDetailsDto);
}
