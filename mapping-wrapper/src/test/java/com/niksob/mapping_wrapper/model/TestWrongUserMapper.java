package com.niksob.mapping_wrapper.model;

import com.niksob.domain.model.user.UserInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TestWrongUserMapper {
    @Mapping(source = "username.value", target = "username")
    @Mapping(source = "nickname.value", target = "nickname")
    @Mapping(source = "password.value", target = "password")
    UserEntity toEntity(UserInfo userInfo);
}
