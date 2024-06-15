package com.niksob.domain.model.mail;

import com.niksob.domain.model.auth.login.active_code.ActiveCode;
import com.niksob.domain.model.user.Email;
import com.niksob.domain.model.user.Username;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActiveCodeMailDetails {
    private final Username username;
    private final Email email;
    private final ActiveCode activeCode;
}
