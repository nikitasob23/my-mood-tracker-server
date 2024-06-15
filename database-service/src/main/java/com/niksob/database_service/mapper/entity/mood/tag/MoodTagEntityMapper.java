package com.niksob.database_service.mapper.entity.mood.tag;

import com.niksob.database_service.entity.mood.tag.MoodTagEntity;
import com.niksob.domain.model.mood.tag.MoodTag;
import com.niksob.domain.model.user.UserId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface MoodTagEntityMapper {
    default Long toEntityUserId(UserId userId) {
        return userId.getValue();
    }

    default Set<MoodTag> fromEntitySet(Set<MoodTagEntity> entities) {
        return entities == null ? null : entities.stream()
                .map(this::fromEntity)
                .collect(Collectors.toSet());
    }

    default Set<MoodTagEntity> toEntitySet(Set<MoodTag> moodTags) {
        return moodTags == null ? null : moodTags.stream()
                .map(this::toEntity)
                .collect(Collectors.toSet());
    }

    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "name.value", target = "name")
    @Mapping(source = "degree.value", target = "degree")
    @Mapping(source = "userId.value", target = "userId")
    MoodTagEntity toEntity(MoodTag moodTag);

    @Mapping(source = "id", target = "id.value")
    @Mapping(source = "name", target = "name.value")
    @Mapping(source = "degree", target = "degree.value")
    @Mapping(source = "userId", target = "userId.value")
    MoodTag fromEntity(MoodTagEntity entity);
}
