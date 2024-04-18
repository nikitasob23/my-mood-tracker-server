package com.niksob.mailsender.model.mail.active_code;

import lombok.Data;
import lombok.NonNull;

@Data
public class ActiveCodeMailSendingRequest {
    @NonNull
    private String senderUsername;
    @NonNull
    private String recipientEmail;
    @NonNull
    private String activeCode;
}
