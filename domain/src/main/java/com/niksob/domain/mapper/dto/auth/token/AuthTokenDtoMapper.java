package com.niksob.domain.mapper.dto.auth.token;

import com.niksob.domain.dto.auth.token.UserAuthTokenDto;
import com.niksob.domain.model.auth.token.UserAuthToken;
import com.niksob.domain.model.user.UserId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthTokenDtoMapper {
    @Mapping(target = "userId", expression = "java(toUserIdDto(authToken.getUserId()))")
    @Mapping(source = "access.value", target = "accessToken")
    @Mapping(source = "refresh.value", target = "refreshToken")
    UserAuthTokenDto toDto(UserAuthToken authToken);

    @Mapping(target = "userId", expression = "java(fromUserIdDto(dto.getUserId()))")
    @Mapping(source = "accessToken", target = "access.value")
    @Mapping(source = "refreshToken", target = "refresh.value")
    UserAuthToken fromDto(UserAuthTokenDto dto);

    default UserId fromUserIdDto(String idValue) {
        final long idLong = Long.parseLong(idValue);
        return new UserId(idLong);
    }

    default String toUserIdDto(UserId userId) {
        return userId.getValue().toString();
    }
}
