package com.niksob.database_service.dao.mood;

import com.niksob.database_service.mapper.entity.mood.entry.DateRangeDaoDtoMapper;
import com.niksob.database_service.mapper.entity.mood.entry.MoodEntryEntityMapper;
import com.niksob.domain.model.mood.entry.UserEntryDateRange;
import com.niksob.domain.model.mood.entry.MoodEntry;
import com.niksob.domain.model.mood.entry.MoodEntryId;
import com.niksob.layer_connector.annotation.LayerConnector;

import java.util.Set;

@LayerConnector(source = MoodEntryWithTagsEntityDao.class,
        mapper = {MoodEntryEntityMapper.class, DateRangeDaoDtoMapper.class})
public interface MoodEntryWithTagsDao {
    Set<MoodEntry> loadByDateRange(UserEntryDateRange userEntryDateRange);

    MoodEntry save(MoodEntry moodEntry);

    MoodEntry saveEntryWithTags(MoodEntry moodEntry);

    void update(MoodEntry moodEntry);

    void updateEntryWithTags(MoodEntry moodEntry);

    void deleteById(MoodEntryId id);
}
