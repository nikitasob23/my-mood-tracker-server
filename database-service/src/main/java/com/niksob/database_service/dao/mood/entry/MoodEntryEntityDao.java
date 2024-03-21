package com.niksob.database_service.dao.mood.entry;

import com.niksob.database_service.entity.mood.entry.MoodEntryEntity;
import com.niksob.database_service.model.mood.entry.date.UserEntryDateRangeDaoDto;

import java.util.Set;

public interface MoodEntryEntityDao {
    Set<MoodEntryEntity> loadByDateRange(UserEntryDateRangeDaoDto userEntityDateRange);

    MoodEntryEntity save(MoodEntryEntity moodEntry);

    void update(MoodEntryEntity moodEntry);

    void deleteById(Long moodEntryId);

}
