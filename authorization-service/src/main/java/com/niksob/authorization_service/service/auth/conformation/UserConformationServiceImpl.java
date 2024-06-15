package com.niksob.authorization_service.service.auth.conformation;

import com.niksob.authorization_service.repository.user.activation.TempActivationUserRepo;
import com.niksob.authorization_service.service.auth.activation.code.ActiveCodeService;
import com.niksob.authorization_service.service.password_encoder.PasswordEncoderService.PasswordEncoderService;
import com.niksob.authorization_service.values.user.DefaultUserInfo;
import com.niksob.domain.exception.auth.signup.active_code.InvalidActiveCodeException;
import com.niksob.domain.http.connector.microservice.mail_sender.MailSenderConnector;
import com.niksob.domain.mapper.dto.user.UserDtoMapper;
import com.niksob.domain.model.auth.login.SignupDetails;
import com.niksob.domain.model.auth.login.active_code.ActiveCode;
import com.niksob.domain.model.user.User;
import com.niksob.domain.model.user.UserInfo;
import com.niksob.domain.model.user.activation.ActivationUserDetails;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class UserConformationServiceImpl implements UserConformationService {
    private final ActiveCodeService activeCodeService;
    private final MailSenderConnector mailSenderConnector;
    private final TempActivationUserRepo tempActivationUserRepo;

    private final PasswordEncoderService passwordEncoderService;
    private final UserDtoMapper userDetailsDtoMapper;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(UserConformationServiceImpl.class);

    @Override
    public Mono<Void> sendSignupActiveCodeMessage(SignupDetails signupDetails) {
        return Mono.fromCallable(() -> createActivationUserDetails(signupDetails))
                .doOnNext(tempActivationUserRepo::save)
                .flatMap(mailSenderConnector::sendSignupMessage)
                .doOnSuccess(ignore -> log.info("Active message sending success", signupDetails))
                .onErrorResume(throwable -> logSendActiveCodeMonoError(throwable, signupDetails));
    }

    @Override
    public Mono<Void> sendEmailResettingActiveCodeMessage(UserInfo userInfo) {
        return Mono.fromCallable(() -> createActivationUserDetails(userInfo))
                .doOnNext(tempActivationUserRepo::save)
                .flatMap(mailSenderConnector::sendEmailResettingMessage)
                .doOnSuccess(ignore -> log.info("Active message sending success", userInfo))
                .onErrorResume(throwable -> logSendActiveCodeMonoError(throwable, userInfo));
    }

    @Override
    public UserInfo getUserInfo(ActiveCode activeCode) {
        final User user = loadActiveUserOrThrow(activeCode);
        return userDetailsDtoMapper.toUserInfo(user);
    }

    private User loadActiveUserOrThrow(ActiveCode activeCode) {
        final User userDetails = tempActivationUserRepo.load(activeCode);
        tempActivationUserRepo.remove(activeCode);
        if (userDetails == null) {
            throw new InvalidActiveCodeException("Wrong activation code");
        }
        return userDetails;
    }

    private <T> Mono<T> logSendActiveCodeMonoError(Throwable throwable, Object state) {
        final String message = "Failure saving and sending activation code";
        log.error(message, throwable, state);
        return Mono.error(throwable);
    }

    private ActivationUserDetails createActivationUserDetails(SignupDetails signupDetails) {
        final User userDetails = new User(
                signupDetails.getEmail(),
                DefaultUserInfo.createUsernameIfEmpty(signupDetails),
                passwordEncoderService.encode(signupDetails.getRowPassword()));
        final ActiveCode activeCode = activeCodeService.generate(userDetails.getEmail());
        return new ActivationUserDetails(activeCode, userDetails);
    }

    private ActivationUserDetails createActivationUserDetails(UserInfo userInfo) {
        final User user = userDetailsDtoMapper.fromUserInfo(userInfo);
        final ActiveCode activeCode = activeCodeService.generate(user.getEmail());
        return new ActivationUserDetails(activeCode, user);
    }
}
