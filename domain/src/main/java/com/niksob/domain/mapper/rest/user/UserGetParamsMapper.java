package com.niksob.domain.mapper.rest.user;

import com.niksob.domain.dto.user.UserIdDto;
import com.niksob.domain.dto.user.UsernameDto;
import org.mapstruct.Mapper;

import java.util.Map;

import static com.niksob.domain.mapper.rest.user.UserRestParamNames.USERNAME_PARAM_KEY;
import static com.niksob.domain.mapper.rest.user.UserRestParamNames.USER_ID_PARAM_NAME;

@Mapper(componentModel = "spring")
public abstract class UserGetParamsMapper {
    public Map<String, String> getHttpParams(UsernameDto username) {
        return Map.of(USERNAME_PARAM_KEY, username.getValue());
    }

    public Map<String, String> getHttpParams(UserIdDto userId) {
        return Map.of(USER_ID_PARAM_NAME, userId.getValue());
    }
}
