package com.niksob.database_service.mapper.dao.mood.entry;

import com.niksob.database_service.entity.mood.entry.MoodEntryEntity;
import com.niksob.database_service.mapper.dao.mood.tag.MoodTagEntityMapper;
import com.niksob.domain.model.mood.entry.MoodEntry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MoodTagEntityMapper.class})
public interface MoodEntryEntityMapper {

    @Mapping(source = "degree.value", target = "degree")
    MoodEntryEntity toEntity(MoodEntry moodEntry);

    @Mapping(source = "degree", target = "degree.value")
    MoodEntry fromEntity(MoodEntryEntity entity);
}
