package com.niksob.domain.http.connector.microservice.auth;

import com.niksob.domain.http.connector.microservice.auth.dto.LoginControllerDtoConnector;
import com.niksob.domain.mapper.dto.auth.login.SignupDetailsDtoMapper;
import com.niksob.domain.model.auth.login.SignupDetails;
import com.niksob.layer_connector.annotation.LayerConnector;
import reactor.core.publisher.Mono;

@LayerConnector(source = LoginControllerDtoConnector.class, mapper = SignupDetailsDtoMapper.class)
public interface LoginControllerConnector {
    Mono<Void> signup(SignupDetails signupDetails);
}
