package com.niksob.domain.http.connector.microservice.mail_sender;

import com.niksob.domain.http.connector.microservice.mail_sender.dto.MailSenderDtoConnector;
import com.niksob.domain.mapper.mail.ActiveCodeMailDetailsDtoMapper;
import com.niksob.domain.model.user.activation.ActivationUserDetails;
import com.niksob.layer_connector.annotation.LayerConnector;
import reactor.core.publisher.Mono;

@LayerConnector(source = MailSenderDtoConnector.class, mapper = ActiveCodeMailDetailsDtoMapper.class)
public interface MailSenderConnector {
    Mono<Void> sendSignupMessage(ActivationUserDetails activationUserDetails);

    Mono<Void> sendEmailResettingMessage(ActivationUserDetails activationUserDetails);
}
