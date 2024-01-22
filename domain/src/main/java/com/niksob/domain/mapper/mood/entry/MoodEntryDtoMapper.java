package com.niksob.domain.mapper.mood.entry;

import com.niksob.domain.dto.mood.entry.MoodEntryDto;
import com.niksob.domain.mapper.mood.tag.MoodTagDtoMapper;
import com.niksob.domain.model.mood.entry.MoodEntry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MoodTagDtoMapper.class})
public interface MoodEntryDtoMapper {

    @Mapping(source = "degree.value", target = "degree")
    MoodEntryDto toDto(MoodEntry moodEntry);

    @Mapping(source = "degree", target = "degree.value")
    MoodEntry fromDto(MoodEntryDto dto);
}
