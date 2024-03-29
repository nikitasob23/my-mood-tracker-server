package com.niksob.domain.mapper.rest.auth.token.params;

import com.niksob.domain.dto.auth.token.details.AuthTokenDetailsDto;
import com.niksob.domain.dto.user.UserIdDto;
import org.mapstruct.Mapper;

import java.util.Map;

@Mapper(componentModel = "spring")
public interface AuthTokenGetParamsMapper {
    String USER_ID_PARAM_NAME = "userId";
    String DEVICE_PARAM_NAME = "device";

    default Map<String, String> getHttpParams(AuthTokenDetailsDto authTokenDetails) {
        return Map.of(
                USER_ID_PARAM_NAME, authTokenDetails.getUserId(),
                DEVICE_PARAM_NAME, authTokenDetails.getDevice()
        );
    }

    default Map<String, String> getHttpParams(UserIdDto userId) {
        return Map.of(USER_ID_PARAM_NAME, userId.getValue());
    }
}
