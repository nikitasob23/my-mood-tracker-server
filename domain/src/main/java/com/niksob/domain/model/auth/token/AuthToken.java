package com.niksob.domain.model.auth.token;

import com.niksob.domain.annotation.Default;
import com.niksob.domain.model.user.UserId;
import lombok.Data;

@Data
public class AuthToken {
    private final AuthTokenId id;
    private final UserId userId;
    private final AccessToken access;
    private final RefreshToken refresh;

    @Default
    public AuthToken(AuthTokenId id, UserId userId, AccessToken access, RefreshToken refresh) {
        this.id = id;
        this.userId = userId;
        this.access = access;
        this.refresh = refresh;
    }

    public AuthToken(UserId userId, AccessToken access, RefreshToken refresh) {
        this.id = null;
        this.userId = userId;
        this.access = access;
        this.refresh = refresh;
    }
}
