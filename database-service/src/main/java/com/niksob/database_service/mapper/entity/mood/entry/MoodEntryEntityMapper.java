package com.niksob.database_service.mapper.entity.mood.entry;

import com.niksob.database_service.entity.mood.entry.MoodEntryEntity;
import com.niksob.database_service.mapper.entity.mood.tag.MoodTagEntityMapper;
import com.niksob.domain.model.mood.entry.MoodEntry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MoodTagEntityMapper.class})
public interface MoodEntryEntityMapper {

    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "degree.value", target = "degree")
    MoodEntryEntity toEntity(MoodEntry moodEntry);

    @Mapping(source = "id", target = "id.value")
    @Mapping(source = "degree", target = "degree.value")
    MoodEntry fromEntity(MoodEntryEntity entity);
}
