package com.niksob.authorization_service.values.user;

import com.niksob.domain.model.user.Nickname;
import com.niksob.domain.model.user.Username;

public class DefaultUserInfo {
    public static Nickname createNickname(Username username) {
        final String[] splitUsername = username.getValue().split("@");
        return new Nickname(splitUsername[0]);
    }
}
