package com.niksob.database_service.dao.mood.tag.cache;

import com.niksob.database_service.dao.mood.tag.loader.CachedMoodTagEntityLoaderDaoImpl;
import com.niksob.database_service.entity.mood.tag.MoodTagEntity;
import com.niksob.domain.exception.resource.ResourceNotFoundException;
import com.niksob.domain.exception.resource.ResourceUpdatingException;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.cache.Cache;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public class MoodTagEntityCacheImpl implements MoodTagEntityCache {
    private final Cache cache;

    private final CachedMoodTagEntityLoaderDaoImpl dao;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(MoodTagEntityCacheImpl.class);

    public void updateCacheCollection(@NonNull MoodTagEntity newMoodTag) {
        Set<MoodTagEntity> dbCollection = getDbCollection(newMoodTag.getUserId());
        updateCacheCollection(dbCollection, newMoodTag);
        updateCacheCollection(newMoodTag.getUserId(), dbCollection);
        log.debug("Mood tag entity cache updated", newMoodTag);
    }

    public void updateCacheCollection(Collection<MoodTagEntity> moodTags) {
        Long userId = getSingleUserId(moodTags);
        final Set<MoodTagEntity> dbCollection = getDbCollection(userId);
        moodTags.forEach(tag -> updateCacheCollection(dbCollection, tag));
        updateCacheCollection(userId, dbCollection);
        log.debug("Mood tag entities cache updated", moodTags);
    }

    public void deleteFromCacheCollection(@NonNull MoodTagEntity deletedMoodTag) {
        Set<MoodTagEntity> dbCollection;
        try {
            dbCollection = dao.loadByUserId(deletedMoodTag.getUserId());
        } catch (ResourceNotFoundException e) {
            log.debug("Do not remove the mood tag from the cache as it does not exist", deletedMoodTag);
            return;
        }
        dbCollection.remove(deletedMoodTag);
        cache.put(deletedMoodTag.getUserId(), dbCollection);
        log.debug("Mood tag entity cache updated", deletedMoodTag);
    }

    private Set<MoodTagEntity> getDbCollection(Long userId) {
        Set<MoodTagEntity> dbCollection;
        try {
            dbCollection = dao.loadByUserId(userId);
        } catch (ResourceNotFoundException e) {
            dbCollection = new HashSet<>();
        }
        return dbCollection;
    }

    private void updateCacheCollection(Set<MoodTagEntity> dbCollection, MoodTagEntity newMoodTag) {
        final Set<MoodTagEntity> tagsToRemove = dbCollection.stream()
                .filter(tag -> tag.getId().equals(newMoodTag.getId()))
                .collect(Collectors.toSet());
        dbCollection.removeAll(tagsToRemove);
        dbCollection.add(newMoodTag);
    }

    private void updateCacheCollection(Long userId, Set<MoodTagEntity> dbCollection) {
        cache.evict(userId);
        cache.put(userId, dbCollection);
    }

    private Long getSingleUserId(Collection<MoodTagEntity> moodTags) {
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
}
