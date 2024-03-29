package com.niksob.domain.mapper.rest.user;

import com.niksob.domain.dto.user.UsernameDto;
import org.mapstruct.Mapper;

import java.util.Map;

@Mapper(componentModel = "spring")
public interface UserGetParamsMapper {
    String USERNAME_PARAM_KEY = "username";

    default Map<String, String> getHttpParams(UsernameDto username) {
        return Map.of(USERNAME_PARAM_KEY, username.getValue());
    }
}
