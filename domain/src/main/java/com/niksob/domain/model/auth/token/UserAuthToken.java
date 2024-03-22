package com.niksob.domain.model.auth.token;

import com.niksob.domain.model.user.UserId;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserAuthToken {
    private final UserId userId;
    private final AccessToken access;
    private final RefreshToken refresh;
}
