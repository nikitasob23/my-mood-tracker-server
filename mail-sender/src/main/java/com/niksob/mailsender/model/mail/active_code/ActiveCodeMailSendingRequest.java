package com.niksob.mailsender.model.mail.active_code;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiveCodeMailSendingRequest {
    @JsonProperty("sender_username")
    private String senderUsername;
    @JsonProperty("recipient_email")
    private String recipientEmail;
    @JsonProperty("active_code")
    private String activeCode;
}
