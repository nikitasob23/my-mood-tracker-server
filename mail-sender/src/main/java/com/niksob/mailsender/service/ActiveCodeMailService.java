package com.niksob.mailsender.service;

import com.niksob.mailsender.model.mail.active_code.ActiveCodeSendingInfo;

public interface ActiveCodeMailService {
    void confirmSignup(ActiveCodeSendingInfo activeCodeSendingInfo);

    void confirmEmailResetting(ActiveCodeSendingInfo activeCodeSendingInfo);
}
