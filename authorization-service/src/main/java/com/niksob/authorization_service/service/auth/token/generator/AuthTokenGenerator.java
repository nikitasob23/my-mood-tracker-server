package com.niksob.authorization_service.service.auth.token.generator;

import com.niksob.domain.model.auth.token.AuthToken;
import com.niksob.domain.model.user.UserInfo;

public interface AuthTokenGenerator {
    AuthToken generate(UserInfo userInfo);
}
