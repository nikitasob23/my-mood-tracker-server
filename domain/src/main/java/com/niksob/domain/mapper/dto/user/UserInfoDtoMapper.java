package com.niksob.domain.mapper.dto.user;

import com.niksob.domain.dto.user.UserInfoDto;
import com.niksob.domain.mapper.dto.mood.entry.MoodEntryDtoMapper;
import com.niksob.domain.model.user.UserInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MoodEntryDtoMapper.class})
public interface UserInfoDtoMapper {
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "username.value", target = "username")
    @Mapping(source = "nickname.value", target = "nickname")
    @Mapping(source = "password.value", target = "password")
    UserInfoDto toDto(UserInfo userInfo);

    @Mapping(source = "id", target = "id.value")
    @Mapping(source = "username", target = "username.value")
    @Mapping(source = "nickname", target = "nickname.value")
    @Mapping(source = "password", target = "password.value")
    UserInfo fromDto(UserInfoDto userInfoDto);
}
