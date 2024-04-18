package com.niksob.domain.http.connector.microservice.database.mood.entry.dto;

import com.niksob.domain.config.properties.microservice.database.DatabaseConnectionProperties;
import com.niksob.domain.dto.mood.entry.MoodEntryDto;
import com.niksob.domain.dto.mood.entry.MoodEntryIdDto;
import com.niksob.domain.dto.mood.entry.UserEntryDateRangeDto;
import com.niksob.domain.http.client.HttpClient;
import com.niksob.domain.http.connector.base.BaseConnector;
import com.niksob.domain.http.connector.microservice.database.error.handler.DatabaseDtoConnectorErrorHandler;
import com.niksob.domain.http.rest.path.RestPath;
import com.niksob.domain.mapper.rest.mood.entry.MoodEntryGetParamsMapper;
import com.niksob.domain.path.controller.database_service.mood.entry.MoodEntryControllerPaths;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class MoodEntryDatabaseDtoConnectorImpl extends BaseConnector implements MoodEntryDatabaseDtoConnector {
    private final MoodEntryGetParamsMapper moodEntryGetParamsMapper;
    private final DatabaseDtoConnectorErrorHandler errorHandler;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(MoodEntryDatabaseDtoConnectorImpl.class);

    public MoodEntryDatabaseDtoConnectorImpl(
            HttpClient httpClient,
            RestPath restPath,
            DatabaseConnectionProperties connectionProperties,
            MoodEntryGetParamsMapper moodEntryGetParamsMapper,
            @Qualifier("moodEntryDatabaseDtoConnectorErrorHandler")
            DatabaseDtoConnectorErrorHandler errorHandler
    ) {
        super(httpClient, restPath, connectionProperties);
        this.moodEntryGetParamsMapper = moodEntryGetParamsMapper;
        this.errorHandler = errorHandler;
    }

    @Override
    public Flux<MoodEntryDto> loadByDateRange(UserEntryDateRangeDto userEntryDateRange) {
        final Map<String, String> params = moodEntryGetParamsMapper.getHttpParams(userEntryDateRange);
        final String uri = getWithParams(MoodEntryControllerPaths.BASE_URI, params);
        return httpClient.sendGetRequestAndReturnFlux(uri, MoodEntryDto.class)
                .doOnNext(moodEntryDto -> log.info("Mood entry load to the connector", null, moodEntryDto))
                .switchIfEmpty(loadAndCreateEmptyMono(userEntryDateRange))
                .onErrorResume(throwable -> errorHandler.createLoadingError(throwable, userEntryDateRange));
    }

    @Override
    public Mono<MoodEntryDto> save(MoodEntryDto moodEntry) {
        return httpClient.sendPostRequest(
                getWithBody(MoodEntryControllerPaths.BASE_URI),
                moodEntry, MoodEntryDto.class, MoodEntryDto.class
        ).onErrorResume(throwable -> errorHandler.createSavingError(throwable, moodEntry));
    }

    @Override
    public Mono<Void> update(MoodEntryDto moodEntry) {
        return httpClient.sendPutRequest(
                getWithBody(MoodEntryControllerPaths.BASE_URI),
                moodEntry, MoodEntryDto.class, Void.class
        ).onErrorResume(throwable -> errorHandler.createUpdatingError(throwable, moodEntry));
    }

    @Override
    public Mono<Void> deleteById(MoodEntryIdDto id) {
        final Map<String, String> params = moodEntryGetParamsMapper.getHttpParams(id);
        final String uri = getWithParams(MoodEntryControllerPaths.BASE_URI, params);
        return httpClient.sendDeleteRequest(uri, Void.class)
                .onErrorResume(throwable -> errorHandler.createUpdatingError(throwable, id));
    }

    private Publisher<? extends MoodEntryDto> loadAndCreateEmptyMono(UserEntryDateRangeDto userEntryDateRangeDto) {
        log.error("Mood entry's list is empty", null, userEntryDateRangeDto);
        return Mono.empty();
    }
}
