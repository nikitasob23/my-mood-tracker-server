package com.niksob.domain.dto.mood.active_code;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiveCodeMailDetailsDto {
    @JsonProperty("sender_username")
    private String senderUsername;
    @JsonProperty("recipient_email")
    private String recipientEmail;
    @JsonProperty("active_code")
    private String activeCode;
}
