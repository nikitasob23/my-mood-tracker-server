package com.niksob.mailsender.service;

import com.niksob.mailsender.model.mail.active_code.ActiveCodeSendingInfo;
import com.niksob.mailsender.model.mail.active_code.message.ActiveCodeMailSubject;
import com.niksob.mailsender.model.mail.active_code.message.ActiveCodeMessageTemplate;
import com.niksob.mailsender.service.base.ExecutableService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class SendActiveCodeByMailService implements ExecutableService<ActiveCodeSendingInfo, Void> {

    @NonNull
    private final JavaMailSender mailSender;

    private final ActiveCodeMessageTemplate activeCodeMessageTemplate;

    private final ActiveCodeMailSubject activeCodeMailSubject;

    @Value("${server.address}")
    private String hostname;

    @Value("${server.port}")
    private String protocol;

    @Value("${spring.mail.username}")
    private String username;

    @Override
    public Void execute(ActiveCodeSendingInfo activeCodeSendingInfo) {
        String message = String.format(
                activeCodeMessageTemplate.data(),
                activeCodeSendingInfo.getSenderUsername().getValue(),
                protocol,
                hostname,
                activeCodeSendingInfo.getActiveCode().data()
        );
        sendActiveCodeTo(activeCodeSendingInfo.getRecipientEmail().value(), message);
        return null;
    }

    private void sendActiveCodeTo(String emailTo, String message) {
        Stream.generate(SimpleMailMessage::new).limit(1)
                .peek(m -> m.setFrom(username))
                .peek(m -> m.setTo(emailTo))
                .peek(m -> m.setSubject(activeCodeMailSubject.data()))
                .peek(m -> m.setText(message))
                .forEach(mailSender::send);
    }
}
