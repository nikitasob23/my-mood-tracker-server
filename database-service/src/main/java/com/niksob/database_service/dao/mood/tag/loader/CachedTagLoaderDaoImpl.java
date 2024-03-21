package com.niksob.database_service.dao.mood.tag.loader;

import com.niksob.database_service.dao.mood.tag.values.MoodTagCacheNames;
import com.niksob.database_service.entity.mood.tag.MoodTagEntity;
import com.niksob.database_service.model.mood.tag.user.UserMoodTagEntities;
import com.niksob.domain.exception.resource.ResourceLoadingException;
import com.niksob.database_service.repository.mood.tag.MoodTagRepository;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@AllArgsConstructor
public class CachedTagLoaderDaoImpl implements TagEntityLoaderDao {
    protected final MoodTagRepository moodTagRepository;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(CachedTagLoaderDaoImpl.class);

    @Override
    @Cacheable(value = MoodTagCacheNames.MOOD_TAG_BY_USER_ID_CACHE_NAME, key = "#userId")
    public UserMoodTagEntities loadByUserId(Long userId) {
        log.debug("Start loading mood tag entities by user id from repository", userId);

        final Set<MoodTagEntity> moodTags;
        try {
            moodTags = moodTagRepository.getByUserId(userId);
        } catch (Exception e) {
            log.error("Failed loading mood tag by user id from repository", null, userId);
            throw new ResourceLoadingException("The mood tags was not load", userId, e);
        }
        log.debug("Mood tag entities loaded from repository", moodTags);
        log.debug("Cached mood tag entities", moodTags);
        return new UserMoodTagEntities(userId, moodTags);
    }
}
