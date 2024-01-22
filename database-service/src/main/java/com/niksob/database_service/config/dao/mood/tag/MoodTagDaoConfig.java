package com.niksob.database_service.config.dao.mood.tag;

import com.niksob.database_service.dao.mood.tag.CachedMoodTagEntityDao;
import com.niksob.database_service.dao.mood.tag.MoodTagEntityDao;
import com.niksob.database_service.repository.mood.tag.MoodTagRepository;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;
import java.util.stream.Stream;

@Configuration
public class MoodTagDaoConfig {
    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(MoodTagDaoConfig.class);

    @Bean
    public MoodTagEntityDao getMoodTagEntityDao(MoodTagRepository moodTagRepository, CacheManager cacheManager) {
        Cache moodTagEntityCache = Stream.of(CachedMoodTagEntityDao.MOOD_TAG_CACHE_ENTITY_NAME)
                .map(cacheManager::getCache)
                .filter(Objects::nonNull)
                .findFirst().orElseThrow(this::createCacheStorageNotFoundException);
        return new CachedMoodTagEntityDao(moodTagRepository, moodTagEntityCache);
    }

    private IllegalStateException createCacheStorageNotFoundException() {
        final IllegalStateException e = new IllegalStateException("Mood tag entity cache storage not found");
        log.error("MoodTagDao instance was not created by cache key",
                e, CachedMoodTagEntityDao.MOOD_TAG_CACHE_ENTITY_NAME
        );
        return e;
    }
}
