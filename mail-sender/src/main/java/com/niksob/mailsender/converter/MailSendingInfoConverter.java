package com.niksob.mailsender.converter;

import com.niksob.domain.model.user.Username;
import com.niksob.mailsender.converter.base.Converter;
import com.niksob.mailsender.model.mail.active_code.ActiveCodeMailSendingRequest;
import com.niksob.mailsender.model.auth.ActiveCode;
import com.niksob.mailsender.model.mail.active_code.ActiveCodeSendingInfo;
import com.niksob.mailsender.model.user.Email;
import org.springframework.stereotype.Component;

@Component
public class MailSendingInfoConverter implements Converter<ActiveCodeMailSendingRequest, ActiveCodeSendingInfo> {
    @Override
    public ActiveCodeSendingInfo convert(ActiveCodeMailSendingRequest activeCodeMailSendingRequest) {
        return new ActiveCodeSendingInfo(
                new Username(activeCodeMailSendingRequest.getSenderUsername()),
                new Email(activeCodeMailSendingRequest.getRecipientEmail()),
                new ActiveCode(activeCodeMailSendingRequest.getActiveCode())
        );
    }
}
