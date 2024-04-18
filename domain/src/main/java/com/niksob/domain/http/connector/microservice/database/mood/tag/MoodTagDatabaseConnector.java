package com.niksob.domain.http.connector.microservice.database.mood.tag;

import com.niksob.domain.http.connector.microservice.database.mood.tag.dto.MoodTagDatabaseDtoConnector;
import com.niksob.domain.mapper.dto.mood.entry.UserEntryDateRangeDtoMapper;
import com.niksob.domain.mapper.dto.mood.tag.MoodTagDtoMapper;
import com.niksob.domain.mapper.dto.mood.tag.MoodTagMonoMapper;
import com.niksob.domain.model.mood.tag.MoodTag;
import com.niksob.domain.model.user.UserId;
import com.niksob.layer_connector.annotation.LayerConnector;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@LayerConnector(source = MoodTagDatabaseDtoConnector.class, mapper = {
        MoodTagDtoMapper.class, UserEntryDateRangeDtoMapper.class, MoodTagMonoMapper.class
})
public interface MoodTagDatabaseConnector {
    Flux<MoodTag> loadByUserId(UserId userId);

    Mono<MoodTag> save(MoodTag moodTag);

    Mono<Void> update(MoodTag MoodTag);

    Mono<Void> deleteById(MoodTag MoodTag);
}
