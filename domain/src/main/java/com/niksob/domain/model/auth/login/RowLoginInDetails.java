package com.niksob.domain.model.auth.login;

import com.niksob.domain.model.user.RowPassword;
import com.niksob.domain.model.user.Username;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RowLoginInDetails {
    private final Username username;
    private final RowPassword rowPassword;
    private final String device;
}
