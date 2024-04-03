package com.niksob.domain.http.connector.microservice.auth.login;

import com.niksob.domain.http.connector.microservice.auth.login.dto.LoginControllerDtoConnector;
import com.niksob.domain.mapper.dto.auth.login.SignOutDetailsDtoMapper;
import com.niksob.domain.mapper.dto.auth.login.SignupDetailsDtoMapper;
import com.niksob.domain.mapper.dto.user.id.UserIdDtoMapper;
import com.niksob.domain.model.auth.login.SignOutDetails;
import com.niksob.domain.model.auth.login.SignupDetails;
import com.niksob.domain.model.user.UserId;
import com.niksob.layer_connector.annotation.LayerConnector;
import reactor.core.publisher.Mono;

@LayerConnector(source = LoginControllerDtoConnector.class, mapper = {
        SignupDetailsDtoMapper.class, SignOutDetailsDtoMapper.class, UserIdDtoMapper.class
})
public interface LoginControllerConnector {
    Mono<Void> signup(SignupDetails signupDetails);

    Mono<Void> signOut(SignOutDetails signOutDetails);

    Mono<Void> signOutAll(UserId userId);
}
