package com.niksob.mailsender.service;

import com.niksob.domain.path.controller.gateway_service.AuthControllerPaths;
import com.niksob.mailsender.model.mail.active_code.ActiveCodeSendingInfo;
import com.niksob.mailsender.model.mail.active_code.message.ActiveCodeMailSubject;
import com.niksob.mailsender.service.base.ExecutableService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static com.niksob.mailsender.values.mail.message.MailMessage.MESSAGE_TEMPLATE;

@Service
@RequiredArgsConstructor
public class SendActiveCodeByMailService implements ExecutableService<ActiveCodeSendingInfo, Void> {

    @NonNull
    private final JavaMailSender mailSender;

    private final ActiveCodeMailSubject activeCodeMailSubject;

    @Value("${microservice.connection.gateway.protocol}")
    private String protocol;
    @Value("${mail.sending.activation-code.confirmation.address}")
    private String hostname;
    @Value("${microservice.connection.gateway.port}")
    private String port;
    @Value("${microservice.connection.gateway.path}")
    private String basePath;


    @Value("${spring.mail.username}")
    private String username;

    @Override
    public Void execute(ActiveCodeSendingInfo activeCodeSendingInfo) {
        final String message = MESSAGE_TEMPLATE.formatted(activeCodeSendingInfo.getSenderUsername()
                .getValue());
        final String messageWithUri = message
                + protocol
                + "://" + hostname
                + ":" + port
                + basePath
                + AuthControllerPaths.BASE_URI + AuthControllerPaths.ACTIVE_CODE
                + "/" + activeCodeSendingInfo.getActiveCode().data();

        sendActiveCodeTo(activeCodeSendingInfo.getRecipientEmail().value(), messageWithUri);
        return null;
    }

    private void sendActiveCodeTo(String emailTo, String message) {
        final SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(username);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(activeCodeMailSubject.data());
        mailMessage.setText(message);
        mailSender.send(mailMessage);
    }
}
