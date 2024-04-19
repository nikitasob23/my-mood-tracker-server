package com.niksob.database_service.mapper.entity.user;

import com.niksob.database_service.entity.user.UserEntity;
import com.niksob.database_service.mapper.entity.mood.entry.MoodEntryEntityMapper;
import com.niksob.database_service.mapper.entity.mood.tag.MoodTagEntityMapper;
import com.niksob.domain.model.user.UserInfo;
import com.niksob.domain.model.user.Username;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MoodEntryEntityMapper.class, MoodTagEntityMapper.class})
public interface UserEntityMapper {
    default String toEntityUsername(Username username) {
        return username.getValue();
    }

    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "email.value", target = "email")
    @Mapping(source = "username.value", target = "username")
    @Mapping(source = "password.value", target = "encodedPassword")
    UserEntity toEntity(UserInfo userInfo);

    @Mapping(source = "id", target = "id.value")
    @Mapping(source = "email", target = "email.value")
    @Mapping(source = "username", target = "username.value")
    @Mapping(source = "encodedPassword", target = "password.value")
    UserInfo fromEntity(UserEntity userEntity);
}
