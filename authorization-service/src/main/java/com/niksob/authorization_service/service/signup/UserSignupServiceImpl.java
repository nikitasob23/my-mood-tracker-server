package com.niksob.authorization_service.service.signup;

import com.niksob.authorization_service.model.signup.SignupDetails;
import com.niksob.authorization_service.service.password_encoder.PasswordEncoderService.PasswordEncoderService;
import com.niksob.authorization_service.values.user.DefaultUserInfo;
import com.niksob.domain.http.connector.UserDatabaseConnector;
import com.niksob.domain.model.user.UserInfo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class UserSignupServiceImpl implements UserSignupService {
    private final UserDatabaseConnector userDatabaseConnector;

    private final PasswordEncoderService passwordEncoderService;

    @Override
    public Mono<Void> execute(SignupDetails signupDetails) {
        return Mono.just(signupDetails)
                .map(details -> new UserInfo(
                        signupDetails.getUsername(),
                        DefaultUserInfo.createNickname(signupDetails.getUsername()),
                        passwordEncoderService.encode(signupDetails.getRowPassword()))
                ).flatMap(userDatabaseConnector::save);
    }
}
