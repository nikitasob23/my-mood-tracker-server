package com.niksob.database_service.controller.mood.entry;

import com.niksob.database_service.mapper.entity.mood.entry.UserEntryDateRangeDtoMapper;
import com.niksob.database_service.service.mood.entry.MoodEntryService;
import com.niksob.domain.dto.mood.entry.UserEntryDateRangeDto;
import com.niksob.domain.dto.mood.entry.MoodEntryDto;
import com.niksob.domain.dto.mood.entry.MoodEntryIdDto;
import com.niksob.domain.mapper.dto.mood.entry.MoodEntryDtoMapper;
import com.niksob.layer_connector.annotation.LayerConnector;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@LayerConnector(source = MoodEntryService.class, mapper = {MoodEntryDtoMapper.class, UserEntryDateRangeDtoMapper.class})
interface MoodEntryControllerService {
    Flux<MoodEntryDto> loadByDateRange(UserEntryDateRangeDto dateRange);

    Mono<MoodEntryDto> save(MoodEntryDto moodEntry);

    Mono<Void> update(MoodEntryDto moodEntry);

    Mono<Void> deleteById(MoodEntryIdDto id);
}
