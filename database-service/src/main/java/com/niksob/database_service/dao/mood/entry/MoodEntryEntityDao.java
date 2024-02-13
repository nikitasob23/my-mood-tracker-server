package com.niksob.database_service.dao.mood.entry;

import com.niksob.database_service.entity.mood.entry.UserEntryDateRangeDaoDto;
import com.niksob.database_service.entity.mood.entry.MoodEntryEntity;

import java.util.Set;

public interface MoodEntryEntityDao {
    Set<MoodEntryEntity> loadByDateRange(UserEntryDateRangeDaoDto userEntityDateRange);

    MoodEntryEntity save(MoodEntryEntity moodEntry);

    void update(MoodEntryEntity moodEntry);

    void deleteById(Long moodEntryId);
}
