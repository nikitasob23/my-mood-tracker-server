package com.niksob.database_service.dao.mood.entry;

import com.niksob.database_service.entity.mood.entry.UserEntryDateRangeDaoDto;
import com.niksob.database_service.entity.mood.entry.MoodEntryEntity;
import com.niksob.database_service.exception.resource.*;
import com.niksob.database_service.repository.mood.entry.MoodEntryEntityRepository;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Component
@AllArgsConstructor
public class MoodEntryEntityDaoImpl implements MoodEntryEntityDao {
    private final MoodEntryEntityRepository moodEntryRepository;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(MoodEntryEntityDaoImpl.class);

    @Override
    public Set<MoodEntryEntity> loadByDateRange(UserEntryDateRangeDaoDto userEntityDateRange) {
        log.debug("Start loading mood entry entities by user id from repository", userEntityDateRange);

        final Set<MoodEntryEntity> moodEntries;
        try {
            moodEntries = moodEntryRepository.loadByDateRange(userEntityDateRange);
        } catch (Exception e) {
            log.error("Failed loading mood entries by user id from repository", null, userEntityDateRange);
            throw new ResourceLoadingException("The mood entries was not load", userEntityDateRange, e);
        }
        if (moodEntries.isEmpty()) {
            log.error("Failed getting mood entries by user id from repository", null, userEntityDateRange);
            throw new ResourceNotFoundException("The mood entries was not found", null, userEntityDateRange);
        }
        log.debug("Mood tag entries loaded from repository", moodEntries);
        log.debug("Cached mood entry entities", moodEntries);
        return moodEntries;
    }

    @Override
    @Transactional
    public MoodEntryEntity save(MoodEntryEntity moodEntry) {
        log.debug("Start saving mood entry entity to repository", moodEntry);
        try {
            final MoodEntryEntity saved = moodEntryRepository.save(moodEntry);
            log.debug("Mood entry entity saved", moodEntry);
            return saved;
        } catch (Exception e) {
            log.error("Failed saving mood entry to repository", e, moodEntry);
            throw new ResourceSavingException("Mood entry has not saved", moodEntry.getId(), e);
        }
    }

    @Override
    @Transactional
    public void update(MoodEntryEntity moodEntry) {
        log.debug("Updating mood entry entity", moodEntry);
        final Optional<MoodEntryEntity> loaded = moodEntryRepository.findById(moodEntry.getId());
        if (loaded.isEmpty()) {
            throw createResourceNotFoundException(moodEntry);
        }
        try {
            final MoodEntryEntity updated = moodEntryRepository.save(moodEntry);
            log.debug("Mood entry entity updated", updated);
        } catch (Exception e) {
            log.error("Failed updating mood entry entity in repository", null, moodEntry);
            throw new ResourceUpdatingException("Mood entry entity has not updated", e, moodEntry.getId());
        }
    }

    @Override
    @Transactional
    public void deleteById(Long moodEntryId) {
        log.debug("Start deleting mood entry entity from repository", moodEntryId);
        if (!moodEntryRepository.existsById(moodEntryId)) {
            throw createResourceNotFoundException(moodEntryId);
        }
        try {
            moodEntryRepository.deleteById(moodEntryId);
            log.debug("Mood entry entity deleted from repository", moodEntryId);
        } catch (Exception e) {
            log.error("Failed deleting mood entry entity by id from repository", null, moodEntryId);
            throw new ResourceDeletionException("The mood entry entity was not deleted", e, moodEntryId);
        }
    }

    private ResourceNotFoundException createResourceNotFoundException(Object state) {
        log.error("Failed getting mood entry entity by id from repository", null, state);
        return new ResourceNotFoundException("The mood entry was not found", null, state);
    }
}
