package com.niksob.domain.mapper.dto.mood.entry;

import com.niksob.domain.dto.mood.entry.MoodEntryDto;
import com.niksob.domain.dto.mood.entry.UserMoodEntryIdDto;
import com.niksob.domain.mapper.dto.mood.tag.MoodTagDtoMapper;
import com.niksob.domain.model.mood.entry.MoodEntry;
import com.niksob.domain.model.mood.entry.UserMoodEntryId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Mapper(componentModel = "spring", uses = {MoodTagDtoMapper.class})
public interface MoodEntryDtoMapper {
    default Flux<MoodEntryDto> toFluxDto(Flux<MoodEntry> flux) {
        return flux.map(this::toDto);
    }

    default Flux<MoodEntry> fromFluxDto(Flux<MoodEntryDto> flux) {
        return flux.map(this::fromDto);
    }

    default Mono<MoodEntryDto> toMonoDto(Mono<MoodEntry> mono) {
        return mono.map(this::toDto);
    }

    default Mono<MoodEntry> fromMonoDto(Mono<MoodEntryDto> mono) {
        return mono.map(this::fromDto);
    }

    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "userId.value", target = "userId")
    @Mapping(source = "degree.value", target = "degree")
    MoodEntryDto toDto(MoodEntry moodEntry);

    @Mapping(source = "id", target = "id.value")
    @Mapping(source = "userId", target = "userId.value")
    @Mapping(source = "degree", target = "degree.value")
    MoodEntry fromDto(MoodEntryDto dto);

    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "userId.value", target = "userId")
    UserMoodEntryIdDto toUserMoodEntryIdDto(UserMoodEntryId userEntryId);

    @Mapping(source = "id", target = "id.value")
    @Mapping(source = "userId", target = "userId.value")
    UserMoodEntryId toUserMoodEntryId(UserMoodEntryIdDto dto);
}
