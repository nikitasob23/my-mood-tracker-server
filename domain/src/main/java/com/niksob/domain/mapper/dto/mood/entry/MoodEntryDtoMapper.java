package com.niksob.domain.mapper.dto.mood.entry;

import com.niksob.domain.dto.mood.entry.MoodEntryDto;
import com.niksob.domain.dto.mood.entry.MoodEntryIdDto;
import com.niksob.domain.mapper.dto.mood.tag.MoodTagDtoMapper;
import com.niksob.domain.model.mood.entry.MoodEntry;
import com.niksob.domain.model.mood.entry.MoodEntryId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Mapper(componentModel = "spring", uses = {MoodTagDtoMapper.class})
public interface MoodEntryDtoMapper {
    default MoodEntryId fromEntryIdDto(MoodEntryIdDto dto) {
        final Long id = Long.parseLong(dto.getValue());
        return new MoodEntryId(id);
    }

    default Flux<MoodEntryDto> toFluxDto(Flux<MoodEntry> flux) {
        return flux.map(this::toDto);
    }

    default Mono<MoodEntryDto> toMonoDto(Mono<MoodEntry> mono) {
        return mono.map(this::toDto);
    }

    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "userId.value", target = "userId")
    @Mapping(source = "degree.value", target = "degree")
    MoodEntryDto toDto(MoodEntry moodEntry);

    @Mapping(source = "id", target = "id.value")
    @Mapping(source = "userId", target = "userId.value")
    @Mapping(source = "degree", target = "degree.value")
    MoodEntry fromDto(MoodEntryDto dto);
}
