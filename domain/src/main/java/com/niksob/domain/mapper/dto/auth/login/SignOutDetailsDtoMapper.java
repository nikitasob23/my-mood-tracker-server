package com.niksob.domain.mapper.dto.auth.login;

import com.niksob.domain.dto.auth.login.SignOutDetailsDto;
import com.niksob.domain.model.auth.login.SignOutDetails;
import com.niksob.domain.model.user.UserId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SignOutDetailsDtoMapper {
    @Mapping(target = "userId", expression = "java(convertUserId(dto.getUserId()))")
    SignOutDetails fromDto(SignOutDetailsDto dto);

    default UserId convertUserId(String userIdStr) {
        final long idValue = Long.parseLong(userIdStr);
        return new UserId(idValue);
    }
}
