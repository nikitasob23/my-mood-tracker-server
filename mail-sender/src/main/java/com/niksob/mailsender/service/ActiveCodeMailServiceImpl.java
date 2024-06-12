package com.niksob.mailsender.service;

import com.niksob.domain.path.controller.gateway_service.AuthControllerPaths;
import com.niksob.mailsender.model.mail.active_code.ActiveCodeSendingInfo;
import com.niksob.mailsender.model.mail.active_code.message.ActiveCodeMailSubject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static com.niksob.mailsender.values.mail.message.MailMessage.MESSAGE_TEMPLATE;

@Service
@RequiredArgsConstructor
public class ActiveCodeMailServiceImpl implements ActiveCodeMailService {

    @NonNull
    private final JavaMailSender mailSender;

    private final ActiveCodeMailSubject activeCodeMailSubject;
    private static final String SIGNUP_CONTROLLER_PATH = AuthControllerPaths.BASE_URI + AuthControllerPaths.ACTIVE_CODE;
    private static final String EMAIL_RESETTING_CONTROLLER_PATH =
            AuthControllerPaths.BASE_URI + AuthControllerPaths.EMAIL_RESETTING_ACTIVATION;

    @Value("${mail.sending.activation-code.confirmation.path.protocol}")
    private String protocol;

    @Value("${mail.sending.activation-code.confirmation.path.hostname}")
    private String hostname;

    @Value("${mail.sending.activation-code.confirmation.path.port:}")
    private String port;

    @Value("${mail.sending.activation-code.confirmation.path.base-path}")
    private String basePath;


    @Value("${spring.mail.username}")
    private String username;

    @Override
    public void confirmSignup(ActiveCodeSendingInfo activeCodeSendingInfo) {
        final String messageWithActiveCodePath = formMessageWithActiveCodePath(
                activeCodeSendingInfo, SIGNUP_CONTROLLER_PATH
        );
        sendActiveCodeTo(activeCodeSendingInfo.getRecipientEmail().value(), messageWithActiveCodePath);
    }

    @Override
    public void confirmEmailResetting(ActiveCodeSendingInfo activeCodeSendingInfo) {
        final String messageWithActiveCodePath = formMessageWithActiveCodePath(
                activeCodeSendingInfo, EMAIL_RESETTING_CONTROLLER_PATH
        );
        sendActiveCodeTo(activeCodeSendingInfo.getRecipientEmail().value(), messageWithActiveCodePath);
    }

    private String formMessageWithActiveCodePath(ActiveCodeSendingInfo activeCodeSendingInfo, String controllerPath) {
        final String username = firstInUpperCase(activeCodeSendingInfo);
        return MESSAGE_TEMPLATE.formatted(username)
                + protocol
                + "://" + hostname
                + (port.isEmpty() ? "" : ":" + port)
                + basePath
                + controllerPath
                + "/" + activeCodeSendingInfo.getActiveCode().value();
    }

    private String firstInUpperCase(ActiveCodeSendingInfo activeCodeSendingInfo) {
        final String username = activeCodeSendingInfo.getSenderUsername().getValue();
        return username.substring(0, 1).toUpperCase() + username.substring(1);
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
