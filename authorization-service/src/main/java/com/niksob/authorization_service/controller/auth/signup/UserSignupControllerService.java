package com.niksob.authorization_service.controller.auth.signup;

import com.niksob.authorization_service.mapper.dto.signup.SignupDetailsDtoMapper;
import com.niksob.authorization_service.service.auth.signup.UserSignupService;
import com.niksob.domain.dto.user.signup.SignupDetailsDto;
import com.niksob.layer_connector.annotation.LayerConnector;
import reactor.core.publisher.Mono;

@LayerConnector(source = UserSignupService.class, mapper = SignupDetailsDtoMapper.class)
public interface UserSignupControllerService {
    Mono<Void> execute(SignupDetailsDto userSignupDetails);
}
