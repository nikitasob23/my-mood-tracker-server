package com.niksob.database_service.mapper.entity.user;

import com.niksob.database_service.entity.mood.entry.MoodEntryEntity;
import com.niksob.database_service.entity.mood.tag.MoodTagEntity;
import com.niksob.database_service.entity.user.UserEntity;
import com.niksob.database_service.mapper.entity.mood.entry.MoodEntryEntityMapper;
import com.niksob.domain.model.user.UserInfo;
import com.niksob.domain.model.user.Username;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {MoodEntryEntityMapper.class})
public interface UserEntityMapper {
    default String toEntityUsername(Username username) {
        return username.getValue();
    }

    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "username.value", target = "username")
    @Mapping(source = "password.value", target = "password")
    @Mapping(target = "moodTags", expression = "java(getMoodTagEntity(userEntity.getMoodEntries()))")
    UserEntity toEntity(UserInfo userInfo);

    @Mapping(source = "id", target = "id.value")
    @Mapping(source = "username", target = "username.value")
    @Mapping(source = "password", target = "password.value")
    UserInfo fromEntity(UserEntity userEntity);

    default Set<MoodTagEntity> getMoodTagEntity(Set<MoodEntryEntity> moodEntryEntities) {
        return moodEntryEntities.stream()
                .flatMap(entity -> entity.getMoodTags().stream())
                .collect(Collectors.toSet());
    }
}
