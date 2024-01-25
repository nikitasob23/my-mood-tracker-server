package com.niksob.database_service.dao.mood.tag;

import com.niksob.database_service.cache.cleaner.CacheCleaner;
import com.niksob.database_service.entity.mood.tag.MoodTagEntity;
import com.niksob.database_service.exception.entity.EntityNotDeletedException;
import com.niksob.database_service.repository.mood.tag.MoodTagRepository;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.Cacheable;

import java.util.Objects;
import java.util.stream.Stream;

@AllArgsConstructor
public class CachedMoodTagEntityDao implements MoodTagEntityDao, CacheCleaner {
    public static final String MOOD_TAG_CACHE_ENTITY_NAME = "mood_tags";
    private final MoodTagRepository moodTagRepository;
    private final Cache cache;
    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(CachedMoodTagEntityDao.class);

    @Override
    @Cacheable(value = CachedMoodTagEntityDao.MOOD_TAG_CACHE_ENTITY_NAME, key = "#name")
    public MoodTagEntity load(String name) {
        log.debug("Start loading user entity by username from repository", name);
        return Stream.of(name)
                .map(moodTagRepository::getByName)
                .filter(Objects::nonNull)
                .peek(moodTagEntity -> log.debug("Mood tag entity loaded from repository", moodTagEntity))
                .peek(moodTagEntity -> log.debug("Cached mood tag entity", moodTagEntity))
                .findFirst().orElseThrow(() -> createEntityNotFoundException(name));
    }

    @Override
    public void deleteByName(String name) {
        log.debug("Start deleting mood tag entity by name from repository", name);
        try {
            Stream.of(name)
                    .peek(cache::evict)
                    .peek(moodTagRepository::deleteMoodTagEntityByName)
                    .peek(entity -> log.debug("Mood tag entity deleted from repository", entity))
                    .forEach(entity -> log.debug("Deleted mood tag entity cache", entity));
        } catch (Exception e) {
            final EntityNotDeletedException entityNotDeletedException =
                    new EntityNotDeletedException("Mood tag entity not delete by name", name);
            log.error("Failed deleting mood tag by name from repository", e, name);
            throw entityNotDeletedException;
        }
    }

    @Override
    public void clearCache() {
        cache.clear();
    }

    private EntityNotFoundException createEntityNotFoundException(String name) {
        final EntityNotFoundException e = new EntityNotFoundException("Mood tag not found by name");
        log.error("Failed loading mood tag by name from repository", e, name);
        return e;
    }
}
