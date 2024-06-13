package com.niksob.database_service.dao.mood.entry;

import com.niksob.database_service.entity.mood.entry.UserMoodEntryEntityId;
import com.niksob.database_service.model.mood.entry.date.UserEntryDateRangeDaoDto;
import com.niksob.database_service.entity.mood.entry.MoodEntryEntity;
import com.niksob.database_service.repository.mood.entry.MoodEntryEntityRepository;
import com.niksob.domain.exception.resource.*;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Component("moodEntryEntityDao")
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
            log.error("Failed loading mood entries by date range from repository", null, userEntityDateRange);
            throw new ResourceLoadingException("The mood entry entities was not load", userEntityDateRange, e);
        }
        log.debug("Mood entry entries loaded from repository", moodEntries);
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
    public void deleteByIdAndUserId(UserMoodEntryEntityId userEntryId) {
        log.debug("Start deleting mood entry entity from repository", userEntryId);
        if (!moodEntryRepository.existsById(userEntryId.getId())) {
            throw createResourceNotFoundException(userEntryId);
        }
        try {
            moodEntryRepository.deleteByIdAndUserId(userEntryId.getId(), userEntryId.getUserId());
            log.debug("Mood entry entity deleted from repository", userEntryId);
        } catch (Exception e) {
            log.error(
                    "Failed deleting mood entry entity by id and user id from repository", null, userEntryId
            );
            throw new ResourceDeletionException("The mood entry entity was not deleted", e, userEntryId);
        }
    }

    @Override
    @Transactional
    public void deleteAllByUserId(Long userId) {
        log.debug("Start deleting all mood entry entities by user id from repository", userId);
        try {
            moodEntryRepository.deleteAllByUserId(userId);
            log.debug("All mood entry entities deleted by user id from repository", userId);
        } catch (Exception e) {
            log.error("Failed deleting all mood entry entities by user id from repository", null, userId);
            throw new ResourceDeletionException("All mood entry entities was not deleted by user id", e, userId);
        }
    }

    private ResourceNotFoundException createResourceNotFoundException(Object state) {
        log.error("Failed getting mood entry entity by id from repository", null, state);
        return new ResourceNotFoundException("The mood entry was not found", null, state);
    }
}
