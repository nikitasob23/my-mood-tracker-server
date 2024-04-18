package com.niksob.mailsender.service;

import com.niksob.domain.path.controller.mail_sender.ActiveCodeMailSenderControllerPaths;
import com.niksob.mailsender.model.mail.active_code.ActiveCodeSendingInfo;
import com.niksob.mailsender.model.mail.active_code.message.ActiveCodeMailSubject;
import com.niksob.mailsender.model.mail.active_code.message.ActiveCodeMessageTemplate;
import com.niksob.mailsender.service.base.ExecutableService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

import static com.niksob.mailsender.values.mail.message.MailMessage.MESSAGE;

@Service
@RequiredArgsConstructor
public class SendActiveCodeByMailService implements ExecutableService<ActiveCodeSendingInfo, Void> {

    @NonNull
    private final JavaMailSender mailSender;

    private final ActiveCodeMessageTemplate activeCodeMessageTemplate;

    private final ActiveCodeMailSubject activeCodeMailSubject;

    @Value("${microservice.connection.gateway.protocol}")
    private String protocol;
    @Value("${microservice.connection.gateway.hostname}")
    private String hostname;
    @Value("${microservice.connection.gateway.port}")
    private String port;
    @Value("${microservice.connection.gateway.path}")
    private String basePath;


    @Value("${spring.mail.username}")
    private String username;

    @Override
    public Void execute(ActiveCodeSendingInfo activeCodeSendingInfo) {
        final String uri = new StringBuilder(protocol)
                .append("://").append(hostname)
                .append(":").append(port)
                .append(basePath)
                .toString();
        final String message = MESSAGE + uri + ActiveCodeMailSenderControllerPaths.BASE_URI;

        sendActiveCodeTo(activeCodeSendingInfo.getRecipientEmail().value(), message);
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
