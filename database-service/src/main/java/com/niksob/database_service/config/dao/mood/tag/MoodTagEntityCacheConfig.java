package com.niksob.database_service.config.dao.mood.tag;

import com.niksob.database_service.dao.mood.tag.cache.MoodTagEntityCache;
import com.niksob.database_service.dao.mood.tag.cache.MoodTagEntityCacheImpl;
import com.niksob.database_service.dao.mood.tag.loader.CachedMoodTagEntityLoaderDaoImpl;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;
import java.util.stream.Stream;

@Configuration
@AllArgsConstructor
public class MoodTagEntityCacheConfig {
    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(MoodTagEntityCacheConfig.class);

    @Bean
    public MoodTagEntityCache getMoodTagEntityCache(CacheManager cacheManager, CachedMoodTagEntityLoaderDaoImpl dao) {
        Cache cache = Stream.of(MoodTagEntityCache.MOOD_TAG_BY_USER_ID_CACHE_NAME)
                .map(cacheManager::getCache)
                .filter(Objects::nonNull)
                .findFirst().orElseThrow(this::createCacheStorageNotFoundException);
        return new MoodTagEntityCacheImpl(cache, dao);
    }

    private IllegalStateException createCacheStorageNotFoundException() {
        final IllegalStateException e = new IllegalStateException("Mood tag entity cache storage not found");
        log.error("MoodTagEntityCache instance was not created by cache key",
                e, MoodTagEntityCache.MOOD_TAG_BY_USER_ID_CACHE_NAME
        );
        return e;
    }
}
