package com.niksob.database_service.mapper.dao.mood.tag;

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

    @Mapping(source = "name.value", target = "name")
    @Mapping(source = "degree.value", target = "degree")
    MoodTagEntity toEntity(MoodTag moodTag);

    @Mapping(source = "name", target = "name.value")
    @Mapping(source = "degree", target = "degree.value")
    MoodTag fromEntity(MoodTagEntity entity);
}
