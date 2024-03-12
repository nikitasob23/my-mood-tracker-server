package com.niksob.database_service.dao.mood.tag;

import com.niksob.database_service.dao.mood.tag.cache.MoodTagEntityCache;
import com.niksob.database_service.dao.mood.tag.loader.CachedMoodTagEntityLoaderDaoImpl;
import com.niksob.database_service.entity.mood.tag.MoodTagEntity;
import com.niksob.database_service.repository.mood.tag.MoodTagRepository;
import com.niksob.domain.exception.resource.*;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class CachedMoodTagEntityDaoImpl extends CachedMoodTagEntityLoaderDaoImpl implements MoodTagEntityDao {
    private final MoodTagEntityCache cache;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(CachedMoodTagEntityDaoImpl.class);

    public CachedMoodTagEntityDaoImpl(MoodTagRepository moodTagRepository, MoodTagEntityCache cache) {
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
            cache.updateCacheCollection(saved);
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
            cache.updateCacheCollection(updated);
        } catch (Exception e) {
            log.error("Failed updating mood tag entity in repository", null, moodTag);
            throw new ResourceUpdatingException("Mood tag entity has not updated", e, moodTag.getId());
        }
        return updated;
    }

    @Override
    @Transactional
    public Set<MoodTagEntity> mergeAll(Set<MoodTagEntity> moodTags) {
        log.debug("Start merging mood tag entities", moodTags);
        try {
            final List<MoodTagEntity> mergedList = moodTagRepository.saveAll(moodTags);
            log.debug("Mood tag entities merged", mergedList);
            cache.updateCacheCollection(mergedList);
            return new HashSet<>(mergedList);
        } catch (Exception e) {
            log.error("Failed merging mood tag entities in repository", null, moodTags);
            throw new ResourceUpdatingException("Mood tag entity has not updated", e, moodTags);
        }
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
            cache.deleteFromCacheCollection(moodTag);
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

    private ResourceNotFoundException createResourceNotFoundException(Object state) {
        log.error("Failed getting mood tag entity by id from repository", null, state);
        return new ResourceNotFoundException("The mood tag was not found", null, state);
    }
}
