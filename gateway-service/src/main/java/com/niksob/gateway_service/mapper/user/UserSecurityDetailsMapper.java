package com.niksob.gateway_service.mapper.user;

import com.niksob.domain.model.user.UserInfo;
import com.niksob.gateway_service.model.user.security.UserSecurityDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserSecurityDetailsMapper {
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "email.value", target = "email")
    @Mapping(source = "username.value", target = "username")
    @Mapping(source = "password.value", target = "encodedPassword")
    UserSecurityDetails toUserDetails(UserInfo userInfo);
}
