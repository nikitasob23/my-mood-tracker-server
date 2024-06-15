package com.niksob.domain.model.auth.login;

import com.niksob.domain.model.user.RowPassword;
import com.niksob.domain.model.user.UserId;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserPasswordPair {
    private final UserId userId;
    private final RowPassword oldRowPassword;
    private final RowPassword newRowPassword;
}
