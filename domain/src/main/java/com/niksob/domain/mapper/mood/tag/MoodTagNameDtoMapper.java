package com.niksob.domain.mapper.mood.tag;

import com.niksob.domain.dto.mood.tag.MoodTagNameDto;
import com.niksob.domain.model.mood.tag.MoodTagName;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MoodTagNameDtoMapper {
    MoodTagName fromDto(MoodTagNameDto dto);
}
