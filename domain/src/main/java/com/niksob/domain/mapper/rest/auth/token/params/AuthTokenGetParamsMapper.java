package com.niksob.domain.mapper.rest.auth.token.params;

import com.niksob.domain.dto.auth.login.SignOutDetailsDto;
import com.niksob.domain.dto.auth.login.active_code.ActiveCodeDto;
import com.niksob.domain.dto.auth.token.details.AuthTokenDetailsDto;
import org.mapstruct.Mapper;

import java.util.Map;

import static com.niksob.domain.mapper.rest.auth.AuthRestParamNames.ACTIVE_CODE_PARAM_NAME;
import static com.niksob.domain.mapper.rest.auth.AuthRestParamNames.DEVICE_PARAM_NAME;
import static com.niksob.domain.mapper.rest.user.UserRestParamNames.USER_ID_PARAM_NAME;

@Mapper(componentModel = "spring")
public abstract class AuthTokenGetParamsMapper {
    public Map<String, String> getHttpParams(AuthTokenDetailsDto authTokenDetails) {
        return Map.of(
                USER_ID_PARAM_NAME, authTokenDetails.getUserId(),
                DEVICE_PARAM_NAME, authTokenDetails.getDevice()
        );
    }

    public Map<String, String> getHttpParams(SignOutDetailsDto signOutDetailsDto) {
        return Map.of(
                USER_ID_PARAM_NAME, signOutDetailsDto.getUserId(),
                DEVICE_PARAM_NAME, signOutDetailsDto.getDevice()
        );
    }

    public Map<String, String> getHttpParams(ActiveCodeDto activeCode) {
        return Map.of(ACTIVE_CODE_PARAM_NAME, activeCode.getValue());
    }
}
