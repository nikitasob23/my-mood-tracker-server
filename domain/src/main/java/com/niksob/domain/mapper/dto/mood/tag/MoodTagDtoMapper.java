package com.niksob.domain.mapper.dto.mood.tag;

import com.niksob.domain.dto.mood.tag.MoodTagDto;
import com.niksob.domain.dto.user.UserIdDto;
import com.niksob.domain.model.mood.tag.MoodTag;
import com.niksob.domain.model.user.UserId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface MoodTagDtoMapper {
    default UserId fromUserIdDto(UserIdDto userIdDto) {
        final Long id = Long.parseLong(userIdDto.getValue());
        return new UserId(id);
    }

    default Set<MoodTag> fromDtoSet(Set<MoodTagDto> dto) {
        return dto == null ? null : dto.stream()
                .map(this::fromDto)
                .collect(Collectors.toSet());
    }

    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "name.value", target = "name")
    @Mapping(source = "degree.value", target = "degree")
    @Mapping(source = "userId.value", target = "userId")
    MoodTagDto toDto(MoodTag moodTag);

    @Mapping(source = "id", target = "id.value")
    @Mapping(source = "name", target = "name.value")
    @Mapping(source = "degree", target = "degree.value")
    @Mapping(source = "userId", target = "userId.value")
    MoodTag fromDto(MoodTagDto dto);
}
