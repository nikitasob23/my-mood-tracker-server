package com.niksob.gateway_service.mapper.user.secure;

import com.niksob.domain.dto.user.UserInfoDto;
import com.niksob.gateway_service.model.user.security.UserSecurityDetails;
import com.niksob.gateway_service.model.user.security.UserWithoutSecurityDetailsDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserWithoutSecurityDetailsDtoMapper {
    @Mapping(source = "securityDetails.id", target = "id")
    @Mapping(source = "securityDetails.email", target = "email")
    @Mapping(source = "userWithoutSecurityDetails.username", target = "username")
    @Mapping(source = "securityDetails.password", target = "password")
    UserInfoDto toUserInfo(
            UserWithoutSecurityDetailsDto userWithoutSecurityDetails, UserSecurityDetails securityDetails
    );
}
