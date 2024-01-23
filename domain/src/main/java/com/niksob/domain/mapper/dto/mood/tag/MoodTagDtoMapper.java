package com.niksob.domain.mapper.dto.mood.tag;

import com.niksob.domain.dto.mood.tag.MoodTagDto;
import com.niksob.domain.model.mood.tag.MoodTag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MoodTagDtoMapper {
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "name.value", target = "name")
    @Mapping(source = "degree.value", target = "degree")
    MoodTagDto toDto(MoodTag moodTag);

    @Mapping(source = "id", target = "id.value")
    @Mapping(source = "name", target = "name.value")
    @Mapping(source = "degree", target = "degree.value")
    MoodTag fromDto(MoodTagDto dto);
}
