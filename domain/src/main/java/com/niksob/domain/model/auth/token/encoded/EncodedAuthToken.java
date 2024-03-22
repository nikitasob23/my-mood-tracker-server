package com.niksob.domain.model.auth.token.encoded;

import com.niksob.domain.model.auth.token.AuthTokenId;
import com.niksob.domain.model.user.UserId;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EncodedAuthToken {
    private final AuthTokenId id;
    private final UserId userId;
    private final EncodedAccessToken access;
    private final EncodedRefreshToken refresh;
    private final String device;
}
