package com.niksob.database_service.mapper.entity.mood.tag;

import com.niksob.database_service.entity.mood.tag.MoodTagEntity;
import com.niksob.domain.model.mood.tag.MoodTag;
import com.niksob.domain.model.mood.tag.MoodTagName;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MoodTagEntityMapper {
    default String toEntityName(MoodTagName name) {
        return name.getValue();
    }

    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "name.value", target = "name")
    @Mapping(source = "degree.value", target = "degree")
    @Mapping(source = "userId.value", target = "user.id")
    MoodTagEntity toEntity(MoodTag moodTag);

    @Mapping(source = "id", target = "id.value")
    @Mapping(source = "name", target = "name.value")
    @Mapping(source = "degree", target = "degree.value")
    @Mapping(source = "user.id", target = "userId.value")
    MoodTag fromEntity(MoodTagEntity entity);
}
