package com.niksob.domain.http.connector.microservice.mail_sender.dto;

import com.niksob.domain.dto.mood.active_code.ActiveCodeMailDetailsDto;
import reactor.core.publisher.Mono;

public interface MailSenderDtoConnector {
    Mono<Void> sendActiveCodeMessage(ActiveCodeMailDetailsDto activeCodeMailDetails);
}
