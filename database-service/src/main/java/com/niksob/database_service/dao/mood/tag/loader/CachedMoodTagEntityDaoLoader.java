package com.niksob.database_service.dao.mood.tag.loader;

import com.niksob.database_service.dao.mood.tag.CachedMoodTagEntityDao;
import com.niksob.database_service.dao.mood.tag.MoodTagEntityDao;
import com.niksob.database_service.entity.mood.tag.MoodTagEntity;
import com.niksob.database_service.exception.resource.ResourceLoadingException;
import com.niksob.database_service.exception.resource.ResourceNotFoundException;
import com.niksob.database_service.repository.mood.tag.MoodTagRepository;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@AllArgsConstructor
public abstract class CachedMoodTagEntityDaoLoader implements MoodTagEntityDao {
    public static final String MOOD_TAG_BY_USER_ID_CACHE_NAME = "mood_tags";
    protected final MoodTagRepository moodTagRepository;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(CachedMoodTagEntityDaoLoader.class);

    @Override
    @Transactional
    @Cacheable(value = CachedMoodTagEntityDao.MOOD_TAG_BY_USER_ID_CACHE_NAME, key = "#userId")
    public Set<MoodTagEntity> loadByUserId(Long userId) {
        log.debug("Start loading mood tag entity by username from repository", userId);

        final Set<MoodTagEntity> moodTags;
        try {
            moodTags = moodTagRepository.getByUserId(userId);
        } catch (Exception e) {
            log.error("Failed loading mood tag by user id from repository", null, userId);
            throw new ResourceLoadingException("The mood tags was not load", userId, e);
        }
        if (moodTags.isEmpty()) {
            log.error("Failed getting mood tag by user id from repository", null, userId);
            throw new ResourceNotFoundException("The mood tags was not found", null, userId);
        }
        log.debug("Mood tag entities loaded from repository", moodTags);
        log.debug("Cached mood tag entities", moodTags);
        return moodTags;
    }
}
