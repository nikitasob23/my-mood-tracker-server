package com.niksob.database_service.dao.mood;

import com.niksob.database_service.model.mood.entry.date.UserEntryDateRangeDaoDto;
import com.niksob.database_service.entity.mood.entry.MoodEntryEntity;

import java.util.Set;

public interface MoodEntryWithTagsEntityDao {
    Set<MoodEntryEntity> loadByDateRange(UserEntryDateRangeDaoDto userEntityDateRange);

    MoodEntryEntity save(MoodEntryEntity moodEntry);

    MoodEntryEntity saveEntryWithTags(MoodEntryEntity moodEntry);

    void update(MoodEntryEntity moodEntry);

    void updateEntryWithTags(MoodEntryEntity moodEntry);

    void deleteById(Long moodEntryId);
}
