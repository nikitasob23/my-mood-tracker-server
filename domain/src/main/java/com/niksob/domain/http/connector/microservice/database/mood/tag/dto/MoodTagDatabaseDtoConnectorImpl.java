package com.niksob.domain.http.connector.microservice.database.mood.tag.dto;

import com.niksob.domain.config.properties.microservice.database.DatabaseConnectionProperties;
import com.niksob.domain.dto.mood.tag.MoodTagDto;
import com.niksob.domain.dto.user.UserIdDto;
import com.niksob.domain.http.client.HttpClient;
import com.niksob.domain.http.connector.base.BaseConnector;
import com.niksob.domain.http.connector.microservice.database.error.handler.DatabaseDtoConnectorErrorHandler;
import com.niksob.domain.http.connector.microservice.database.mood.entry.dto.MoodEntryDatabaseDtoConnectorImpl;
import com.niksob.domain.http.rest.path.RestPath;
import com.niksob.domain.mapper.rest.mood.tag.MoodTagGetParamsMapper;
import com.niksob.domain.path.controller.database_service.mood.tag.MoodTagControllerPaths;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class MoodTagDatabaseDtoConnectorImpl extends BaseConnector implements MoodTagDatabaseDtoConnector {
    private final MoodTagGetParamsMapper moodTagGetParamsMapper;
    private final DatabaseDtoConnectorErrorHandler errorHandler;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(MoodEntryDatabaseDtoConnectorImpl.class);

    public MoodTagDatabaseDtoConnectorImpl(
            HttpClient httpClient,
            RestPath restPath,
            DatabaseConnectionProperties connectionProperties,
            MoodTagGetParamsMapper moodTagGetParamsMapper,
            @Qualifier("moodTagDatabaseDtoConnectorErrorHandler")
            DatabaseDtoConnectorErrorHandler errorHandler
    ) {
        super(httpClient, restPath, connectionProperties);
        this.moodTagGetParamsMapper = moodTagGetParamsMapper;
        this.errorHandler = errorHandler;
    }

    @Override
    public Flux<MoodTagDto> loadByUserId(UserIdDto userId) {
        final Map<String, String> params = moodTagGetParamsMapper.getHttpParams(userId);
        final String uri = getWithParams(MoodTagControllerPaths.BASE_URI, params);
        return httpClient.sendGetRequestAndReturnFlux(uri, MoodTagDto.class)
                .doOnNext(moodTagDto -> log.info("Mood entry load to the connector", null, moodTagDto))
                .switchIfEmpty(loadAndCreateEmptyMono(userId))
                .onErrorResume(throwable -> errorHandler.createLoadingError(throwable, userId));
    }

    @Override
    public Mono<MoodTagDto> save(MoodTagDto moodTag) {
        final String uri = getWithBody(MoodTagControllerPaths.BASE_URI);
        return httpClient.sendPostRequest(uri, moodTag, MoodTagDto.class, MoodTagDto.class)
                .onErrorResume(throwable -> errorHandler.createSavingError(throwable, moodTag));
    }

    @Override
    public Mono<Void> update(MoodTagDto moodTag) {
        return httpClient.sendPutRequest(
                getWithBody(MoodTagControllerPaths.BASE_URI),
                moodTag, MoodTagDto.class, Void.class
        ).onErrorResume(throwable -> errorHandler.createUpdatingError(throwable, moodTag));
    }

    @Override
    public Mono<Void> deleteById(MoodTagDto moodTag) {
        final Map<String, String> params = moodTagGetParamsMapper.getHttpParams(moodTag);
        final String uri = getWithParams(MoodTagControllerPaths.BASE_URI, params);
        return httpClient.sendDeleteRequest(uri, Void.class)
                .onErrorResume(throwable -> errorHandler.createUpdatingError(throwable, moodTag));
    }

    private Publisher<? extends MoodTagDto> loadAndCreateEmptyMono(UserIdDto userId) {
        log.error("Mood tag's list is empty", null, userId);
        return Mono.empty();
    }
}
