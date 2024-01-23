package com.niksob.database_service.controller.mood.tag;

import com.niksob.database_service.service.mood.tag.MoodTagService;
import com.niksob.domain.dto.mood.tag.MoodTagDto;
import com.niksob.domain.dto.mood.tag.MoodTagNameDto;
import com.niksob.domain.mapper.dto.mood.tag.MoodTagDtoMapper;
import com.niksob.domain.mapper.dto.mood.tag.MoodTagMonoMapper;
import com.niksob.domain.mapper.dto.mood.tag.MoodTagNameDtoMapper;
import com.niksob.layer_connector.annotation.LayerConnector;
import reactor.core.publisher.Mono;

@LayerConnector(source = MoodTagService.class, mapper = {MoodTagNameDtoMapper.class, MoodTagDtoMapper.class, MoodTagMonoMapper.class})
public interface MoodTagControllerService {
    Mono<MoodTagDto> load(MoodTagNameDto nameDto);

    Mono<Void> delete(MoodTagNameDto nameDto);
}
