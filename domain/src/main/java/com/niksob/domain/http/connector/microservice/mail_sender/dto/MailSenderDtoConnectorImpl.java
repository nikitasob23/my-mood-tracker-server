package com.niksob.domain.http.connector.microservice.mail_sender.dto;

import com.niksob.domain.config.properties.microservice.mail_sender.MailSenderConnectionProperties;
import com.niksob.domain.dto.mood.active_code.ActiveCodeMailDetailsDto;
import com.niksob.domain.exception.server.error.InternalServerException;
import com.niksob.domain.http.client.HttpClient;
import com.niksob.domain.http.connector.base.BaseConnector;
import com.niksob.domain.http.rest.path.RestPath;
import com.niksob.domain.path.controller.mail_sender.ActiveCodeMailSenderControllerPaths;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class MailSenderDtoConnectorImpl extends BaseConnector implements MailSenderDtoConnector {
    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(MailSenderDtoConnectorImpl.class);

    public MailSenderDtoConnectorImpl(
            HttpClient httpClient,
            RestPath restPath,
            MailSenderConnectionProperties connectionProperties
    ) {
        super(httpClient, restPath, connectionProperties);
    }

    @Override
    public Mono<Void> sendSignupMessage(ActiveCodeMailDetailsDto activeCodeMailDetails) {
        final String uri = getWithBody(
                ActiveCodeMailSenderControllerPaths.BASE_URI + ActiveCodeMailSenderControllerPaths.SIGNUP
        );
        return httpClient.sendPostRequest(uri, activeCodeMailDetails, ActiveCodeMailDetailsDto.class, Void.class)
                .onErrorResume(throwable -> createInternalServerMonoError(throwable, activeCodeMailDetails));
    }

    @Override
    public Mono<Void> sendEmailResettingMessage(ActiveCodeMailDetailsDto activeCodeMailDetails) {
        final String uri = getWithBody(
                ActiveCodeMailSenderControllerPaths.BASE_URI + ActiveCodeMailSenderControllerPaths.EMAIL_RESETTING
        );
        return httpClient.sendPostRequest(uri, activeCodeMailDetails, ActiveCodeMailDetailsDto.class, Void.class)
                .onErrorResume(throwable -> createInternalServerMonoError(throwable, activeCodeMailDetails));
    }

    private <T> Mono<T> createInternalServerMonoError(Throwable throwable, Object state) {
        final String message = "Sending active code error";
        log.error(message, null, state);
        return Mono.error(new InternalServerException(message, throwable));
    }
}
