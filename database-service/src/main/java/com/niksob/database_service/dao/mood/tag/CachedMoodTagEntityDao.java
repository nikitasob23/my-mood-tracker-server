package com.niksob.database_service.dao.mood.tag;

import com.niksob.database_service.dao.mood.tag.loader.CachedMoodTagEntityDaoLoader;
import com.niksob.database_service.entity.mood.tag.MoodTagEntity;
import com.niksob.database_service.exception.resource.*;
import com.niksob.database_service.repository.mood.tag.MoodTagRepository;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.NonNull;
import org.springframework.cache.Cache;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

public class CachedMoodTagEntityDao extends CachedMoodTagEntityDaoLoader {
    private final Cache cache;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(CachedMoodTagEntityDao.class);

    public CachedMoodTagEntityDao(MoodTagRepository moodTagRepository, Cache cache) {
        super(moodTagRepository);
        this.cache = cache;
    }

    @Override
    @Transactional
    // Cacheable
    public MoodTagEntity save(MoodTagEntity moodTag) {
        log.debug("Start saving mood tag entity to repository", moodTag);
        if (super.loadByUserId(moodTag.getUserId()).stream()
                .map(MoodTagEntity::getName)
                .anyMatch(moodTag.getName()::equals)) {
            log.error("Failed saving mood tag to repository", null, moodTag);
            throw new ResourceAlreadyExistsException("Mood tag entity already exists", null, moodTag.getName());
        }
        try {
            final MoodTagEntity saved = moodTagRepository.save(moodTag);
            log.debug("Mood tag entity saved", moodTag);
            updateCacheCollection(saved);
            return saved;
        } catch (Exception e) {
            log.error("Failed saving mood tag to repository", e, moodTag);
            throw new ResourceSavingException("Mood tag has not saved", moodTag.getName(), e);
        }
    }

    @Override
    @Transactional
    // Cacheable
    public MoodTagEntity update(MoodTagEntity moodTag) {
        log.debug("Updating mood tag entity", moodTag);
        if (nonPresentInRepoById(moodTag)) {
            throw createResourceNotFoundException(moodTag);
        }
        final MoodTagEntity updated;
        try {
            updated = moodTagRepository.save(moodTag);
            log.debug("Mood tag entity updated", updated);
            updateCacheCollection(updated);
        } catch (Exception e) {
            log.error("Failed updating mood tag entity in repository", null, moodTag);
            throw new ResourceUpdatingException("Mood tag entity has not updated", e, moodTag.getId());
        }
        return updated;
    }

    @Override
    @Transactional
    public void deleteById(MoodTagEntity moodTag) {
        log.debug("Start deleting mood tag entity by moodTag from repository", moodTag);
        if (nonPresentInRepoById(moodTag)) {
            throw createResourceNotFoundException(moodTag);
        }
        try {
            moodTagRepository.deleteById(moodTag.getId());
            log.debug("Mood tag entity deleted from repository", moodTag);
            deleteFromCacheCollection(moodTag);
        } catch (Exception e) {
            log.error("Failed deleting mood tag entity by id from repository", null, moodTag.getId());
            throw new ResourceDeletionException("The mood tag entity was not deleted", e, moodTag.getId());
        }
    }
    
    private boolean nonPresentInRepoById(MoodTagEntity moodTag) {
        return super.loadByUserId(moodTag.getUserId()).stream()
                .map(MoodTagEntity::getId)
                .noneMatch(moodTag.getId()::equals);
        
    }

    private void updateCacheCollection(@NonNull MoodTagEntity newMoodTag) {
        Set<MoodTagEntity> dbCollection;
        try {
            dbCollection = super.loadByUserId(newMoodTag.getUserId());
        } catch (ResourceNotFoundException e) {
            dbCollection = new HashSet<>();
        }
        dbCollection.add(newMoodTag);
        cache.put(newMoodTag.getUserId(), dbCollection);
        log.debug("Mood tag entity cache updated", newMoodTag);
    }

    private void deleteFromCacheCollection(@NonNull MoodTagEntity deletedMoodTag) {
        Set<MoodTagEntity> dbCollection;
        try {
            dbCollection = super.loadByUserId(deletedMoodTag.getUserId());
        } catch (ResourceNotFoundException e) {
            log.debug("Do not remove the mood tag from the cache as it does not exist", deletedMoodTag);
            return;
        }
        dbCollection.remove(deletedMoodTag);
        cache.put(deletedMoodTag.getUserId(), dbCollection);
        log.debug("Mood tag entity cache updated", deletedMoodTag);
    }

    private ResourceNotFoundException createResourceNotFoundException(Object state) {
        log.error("Failed getting mood tag entity by id from repository", null, state);
        return new ResourceNotFoundException("The mood tag was not found", null, state);
    }
}
