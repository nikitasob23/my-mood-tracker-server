package com.niksob.gateway_service.controller.mood.entry;

import com.niksob.domain.dto.mood.entry.MoodEntryDto;
import com.niksob.domain.dto.mood.entry.UserEntryDateRangeDto;
import com.niksob.domain.dto.mood.entry.UserMoodEntryIdDto;
import com.niksob.domain.mapper.dto.mood.entry.MoodEntryDtoMapper;
import com.niksob.domain.mapper.dto.mood.entry.UserEntryDateRangeDtoMapper;
import com.niksob.gateway_service.service.mood.entry.MoodEntryService;
import com.niksob.layer_connector.annotation.LayerConnector;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@LayerConnector(source = MoodEntryService.class, mapper = {MoodEntryDtoMapper.class, UserEntryDateRangeDtoMapper.class})
public interface MoodEntryControllerService {
    Flux<MoodEntryDto> loadByDateRange(UserEntryDateRangeDto userEntryDateRange);

    Mono<MoodEntryDto> save(MoodEntryDto moodEntryDto);

    Mono<Void> update(MoodEntryDto moodEntryDto);

    Mono<Void> deleteByIdAndUserId(UserMoodEntryIdDto userEntryId);

}
