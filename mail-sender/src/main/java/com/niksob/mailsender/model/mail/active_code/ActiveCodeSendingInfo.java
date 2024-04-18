package com.niksob.mailsender.model.mail.active_code;

import com.niksob.domain.model.user.Username;
import com.niksob.mailsender.model.auth.ActiveCode;
import com.niksob.mailsender.model.user.Email;
import lombok.Data;
import lombok.NonNull;

@Data
public class ActiveCodeSendingInfo {
    @NonNull
    private Username senderUsername;
    @NonNull
    private Email recipientEmail;
    @NonNull
    private ActiveCode activeCode;
}
