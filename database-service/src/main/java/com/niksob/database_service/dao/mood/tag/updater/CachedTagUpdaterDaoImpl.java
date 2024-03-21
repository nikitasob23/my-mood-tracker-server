package com.niksob.database_service.dao.mood.tag.updater;

import com.niksob.database_service.dao.mood.tag.loader.TagEntityLoaderDao;
import com.niksob.database_service.dao.mood.tag.values.MoodTagCacheNames;
import com.niksob.database_service.entity.mood.tag.MoodTagEntity;
import com.niksob.database_service.model.mood.tag.user.UserMoodTagEntities;
import com.niksob.database_service.repository.mood.tag.MoodTagRepository;
import com.niksob.domain.exception.resource.*;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CachedTagUpdaterDaoImpl implements TagEntityUpdaterDao {
    private final TagEntityLoaderDao loaderDao;
    private final MoodTagRepository moodTagRepository;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(CachedTagUpdaterDaoImpl.class);

    @Override
    @Transactional
    @CachePut(value = MoodTagCacheNames.MOOD_TAG_BY_USER_ID_CACHE_NAME, key = "#result.userId")
    public UserMoodTagEntities save(MoodTagEntity moodTag) {
        log.info("Start saving mood tag entity to repository", moodTag);
        final UserMoodTagEntities storedUserTags = loaderDao.loadByUserId(moodTag.getUserId());
        if (storedUserTags.getTags().stream()
                .map(MoodTagEntity::getName)
                .anyMatch(moodTag.getName()::equals)) {
            log.error("Failed saving mood tag to repository", null, moodTag);
            throw new ResourceAlreadyExistsException("Mood tag entity already exists", null, moodTag.getName());
        }
        final MoodTagEntity saved;
        try {
            saved = moodTagRepository.save(moodTag);
            log.info("Mood tag entity saved", moodTag);
        } catch (Exception e) {
            log.error("Failed saving mood tag to repository", e, moodTag);
            throw new ResourceSavingException("Mood tag has not saved", moodTag.getName(), e);
        }
        storedUserTags.add(saved);
        log.info("Mood tag entity saved to the cache", null, storedUserTags);
        return storedUserTags;
    }

    @Override
    @Transactional
    @CachePut(value = MoodTagCacheNames.MOOD_TAG_BY_USER_ID_CACHE_NAME, key = "#result.userId")
    public UserMoodTagEntities update(MoodTagEntity tag) {
        log.info("Updating mood tag entity", tag);
        final UserMoodTagEntities storedTags = loaderDao.loadByUserId(tag.getUserId());

        if (nonPresentInRepoById(tag, storedTags)) {
            throw createResourceNotFoundException(tag);
        }
        final MoodTagEntity updated;
        final MoodTagEntity loadedTag;
        try {
            loadedTag = loaderDao.loadByUserId(tag.getUserId())
                    .getTags().stream()
                    .filter(loaded -> loaded.getId().equals(tag.getId()))
                    .findFirst().orElse(null);
            updated = moodTagRepository.save(tag);
            log.info("Mood tag entity updated", updated);
        } catch (Exception e) {
            log.error("Failed updating mood tag entity in repository", null, tag);
            throw new ResourceUpdatingException("Mood tag entity has not updated", e, tag.getId());
        }

        storedTags.update(loadedTag, updated);
        log.info("Mood tag entity cache updated", storedTags);
        return storedTags;
    }

    @Override
    @Transactional
    public UserMoodTagEntities mergeAll(Set<MoodTagEntity> moodTags) {
        log.info("Start merging mood tag entities", moodTags);
        final Long singleUserId = extractSingleUserId(moodTags);
        final UserMoodTagEntities storedTags = loaderDao.loadByUserId(singleUserId);

        final List<MoodTagEntity> mergedList;
        try {
            mergedList = moodTagRepository.saveAll(moodTags);
            log.info("Mood tag entities merged", mergedList);
        } catch (Exception e) {
            log.error("Failed merging mood tag entities in repository", null, moodTags);
            throw new ResourceUpdatingException("Mood tag entity has not updated", e, moodTags);
        }
        storedTags.updateAll(moodTags, mergedList);
        return storedTags;
    }

    @Override
    @Transactional
    @CacheEvict(value = MoodTagCacheNames.MOOD_TAG_BY_USER_ID_CACHE_NAME, key = "#result.userId")
    public UserMoodTagEntities deleteById(MoodTagEntity moodTag) {
        log.info("Start deleting mood tag entity by moodTag from repository", moodTag);
        final UserMoodTagEntities storedTags = loaderDao.loadByUserId(moodTag.getUserId());

        if (nonPresentInRepoById(moodTag, storedTags)) {
            throw createResourceNotFoundException(moodTag);
        }
        try {
            moodTagRepository.deleteById(moodTag.getId());
            log.info("Mood tag entity deleted from repository", moodTag);
        } catch (Exception e) {
            log.error("Failed deleting mood tag entity by id from repository", null, moodTag.getId());
            throw new ResourceDeletionException("The mood tag entity was not deleted", e, moodTag.getId());
        }
        storedTags.remove(moodTag);
        log.info("Mood tag entity deleted from cache", moodTag);
        return storedTags;
    }

    private boolean nonPresentInRepoById(MoodTagEntity moodTag, UserMoodTagEntities storedTags) {
        return storedTags.getTags().stream()
                .map(MoodTagEntity::getId)
                .noneMatch(moodTag.getId()::equals);
    }

    private Long extractSingleUserId(Collection<MoodTagEntity> moodTags) {
        final Set<Long> userIds = moodTags.stream()
                .map(MoodTagEntity::getUserId)
                .collect(Collectors.toSet());
        if (userIds.isEmpty()) {
            log.error("Failed merging mood tag entities in repository", null, moodTags);
            throw new ResourceUpdatingException(
                    "Mood tag entities not merged because they haven't ids", null, userIds);
        }
        if (userIds.size() > 1) {
            log.error("Failed merging mood tag entities in repository", null, moodTags);
            throw new ResourceUpdatingException(
                    "Mood tag entities not merged because they have different ids", null, userIds);
        }
        return userIds.iterator().next();
    }

    private ResourceNotFoundException createResourceNotFoundException(Object state) {
        log.error("Failed getting mood tag entity by id from repository", null, state);
        return new ResourceNotFoundException("The mood tag was not found", null, state);
    }
}
