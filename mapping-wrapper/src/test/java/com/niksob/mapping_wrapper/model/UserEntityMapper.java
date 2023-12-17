package com.niksob.mapping_wrapper.model;

import com.niksob.domain.model.user.UserInfo;
import com.niksob.domain.model.user.Username;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {
    default String toEntityUsername(Username username) {
        return username.getValue();
    }

    default Username toEntityUsername(String value) {
        return new Username(value);
    }

    @Mapping(source = "username.value", target = "username")
    @Mapping(source = "nickname.value", target = "nickname")
    @Mapping(source = "password.value", target = "password")
    UserEntity toEntity(UserInfo userInfo);

    @Mapping(source = "username", target = "username.value")
    @Mapping(source = "nickname", target = "nickname.value")
    @Mapping(source = "password", target = "password.value")
    UserInfo fromEntity(UserEntity userEntity);
}