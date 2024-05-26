package com.niksob.domain.mapper.dto.auth.login;

import com.niksob.domain.dto.auth.login.UserPasswordPairDto;
import com.niksob.domain.model.auth.login.UserPasswordPair;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserPasswordPairDtoMapper {
    @Mapping(source = "userId.value", target = "userId")
    @Mapping(source = "oldRowPassword.value", target = "oldRowPassword")
    @Mapping(source = "newRowPassword.value", target = "newRowPassword")
    UserPasswordPairDto toDto(UserPasswordPair pair);

    @Mapping(source = "userId", target = "userId.value")
    @Mapping(source = "oldRowPassword", target = "oldRowPassword.value")
    @Mapping(source = "newRowPassword", target = "newRowPassword.value")
    UserPasswordPair fromDto(UserPasswordPairDto dto);
}
