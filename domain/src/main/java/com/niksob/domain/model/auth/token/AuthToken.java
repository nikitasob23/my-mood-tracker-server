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
    private String device;

    @Default
    public AuthToken(AuthTokenId id, UserId userId, AccessToken access, RefreshToken refresh, String device) {
        this.id = id;
        this.userId = userId;
        this.access = access;
        this.refresh = refresh;
        this.device = device;
    }

    public AuthToken(UserId userId, AccessToken access, RefreshToken refresh, String device) {
        this.id = null;
        this.userId = userId;
        this.access = access;
        this.refresh = refresh;
        this.device = device;
    }
}
