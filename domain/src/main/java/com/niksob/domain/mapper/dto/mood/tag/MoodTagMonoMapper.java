package com.niksob.domain.mapper.dto.mood.tag;

import com.niksob.domain.dto.mood.tag.MoodTagDto;
import com.niksob.domain.model.mood.tag.MoodTag;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class MoodTagMonoMapper {
    private final MoodTagDtoMapper moodTagDtoMapper;

    public Mono<MoodTagDto> toDtoMono(Mono<MoodTag> mono) {
        return mono.map(moodTagDtoMapper::toDto);
    }

    public Mono<Void> toMonoVoid(Mono<MoodTag> mono) {
        return mono.then();
    }
}
