package com.niksob.database_service.dao.mood.tag;

import com.niksob.database_service.entity.mood.tag.MoodTagEntity;
import com.niksob.database_service.exception.resource.ResourceAlreadyExistsException;
import com.niksob.database_service.exception.resource.ResourceLoadingException;
import com.niksob.database_service.exception.resource.ResourceNotFoundException;
import com.niksob.database_service.exception.resource.ResourceSavingException;
import com.niksob.database_service.repository.mood.tag.MoodTagRepository;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public class CachedMoodTagEntityDao implements MoodTagEntityDao {
    public static final String MOOD_TAG_BY_USER_ID_CACHE_NAME = "mood_tags";

    private final MoodTagRepository moodTagRepository;

    private final Cache cache;
    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(CachedMoodTagEntityDao.class);

    @Override
    @Transactional
    @Cacheable(value = CachedMoodTagEntityDao.MOOD_TAG_BY_USER_ID_CACHE_NAME, key = "#userId")
    public Set<MoodTagEntity> loadByUserId(Long userId) {
        log.debug("Start loading mood tag entity by username from repository", userId);

        final Set<MoodTagEntity> moodTags;
        try {
            moodTags = moodTagRepository.getByUserId(userId);
        } catch (Exception e) {
            throw createResourceLoadingException(e, userId);
        }
        if (moodTags.isEmpty()) {
            throw createResourceNotFoundException(userId);
        }
        log.debug("Mood tag entities loaded from repository", moodTags);
        log.debug("Cached mood tag entities", moodTags);
        return moodTags;
    }

    @Override
    @Transactional
    // Cacheable
    public MoodTagEntity save(MoodTagEntity moodTag) {
        log.debug("Start saving mood tag entity to repository", moodTag);
        if (moodTagRepository.existsByName(moodTag.getName())) {
            var existsException = new ResourceAlreadyExistsException("Mood tag entity already exists", null, moodTag.getName());
            log.error("Failed saving mood tag to repository", null, moodTag);
            throw existsException;
        }
        try {
            moodTagRepository.save(moodTag.getName(), moodTag.getDegree(), moodTag.getUser().getId());
            var saved = moodTagRepository.getByName(moodTag.getName());
            log.debug("Mood tag entity saved", moodTag);
            addMoodTagToCachedCollection(saved);
            return saved;
        } catch (Exception e) {
            var savingException = new ResourceSavingException("Mood tag has not saved", moodTag.getName(), e);
            log.error("Failed saving mood tag to repository", e, moodTag);
            throw savingException;
        }
    }

    private void addMoodTagToCachedCollection(MoodTagEntity moodTag) {
        final Long userId = moodTag.getUser().getId();
        final Cache.ValueWrapper wrapper = cache.get(userId);
        Set<MoodTagEntity> moodTags = wrapper == null ? new HashSet<>() : (Set<MoodTagEntity>) wrapper.get();
        moodTags.add(moodTag);
        cache.put(userId, moodTags);
        log.debug("Mood tag entity cache updated", moodTag);
    }

    private ResourceLoadingException createResourceLoadingException(Exception e, Object state) {
        var loadingException = new ResourceLoadingException("The mood tags was not load", state, e);
        log.error("Failed loading mood tag by user id from repository", loadingException, state);
        return loadingException;
    }

    private ResourceNotFoundException createResourceNotFoundException(Object state) {
        var notFoundException = new ResourceNotFoundException("The mood tags was not found", null, state);
        log.error("Failed getting mood tag by user id from repository", notFoundException, state);
        return notFoundException;
    }
}
