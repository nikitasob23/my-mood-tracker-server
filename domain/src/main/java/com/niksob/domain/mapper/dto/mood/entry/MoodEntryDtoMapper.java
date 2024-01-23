package com.niksob.domain.mapper.dto.mood.entry;

import com.niksob.domain.dto.mood.entry.MoodEntryDto;
import com.niksob.domain.mapper.dto.mood.tag.MoodTagDtoMapper;
import com.niksob.domain.model.mood.entry.MoodEntry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MoodTagDtoMapper.class})
public interface MoodEntryDtoMapper {

    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "degree.value", target = "degree")
    MoodEntryDto toDto(MoodEntry moodEntry);

    @Mapping(source = "id", target = "id.value")
    @Mapping(source = "degree", target = "degree.value")
    MoodEntry fromDto(MoodEntryDto dto);
}
