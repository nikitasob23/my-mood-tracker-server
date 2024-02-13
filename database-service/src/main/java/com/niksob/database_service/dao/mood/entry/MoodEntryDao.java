package com.niksob.database_service.dao.mood.entry;

import com.niksob.database_service.mapper.entity.mood.entry.DateRangeDaoDtoMapper;
import com.niksob.database_service.mapper.entity.mood.entry.MoodEntryEntityMapper;
import com.niksob.domain.model.mood.entry.UserEntryDateRange;
import com.niksob.domain.model.mood.entry.MoodEntry;
import com.niksob.domain.model.mood.entry.MoodEntryId;
import com.niksob.layer_connector.annotation.LayerConnector;

import java.util.Set;

@LayerConnector(source = MoodEntryEntityDao.class, mapper = {MoodEntryEntityMapper.class, DateRangeDaoDtoMapper.class})
public interface MoodEntryDao {
    Set<MoodEntry> loadByDateRange(UserEntryDateRange userEntryDateRange);

    MoodEntry save(MoodEntry moodEntry);

    void update(MoodEntry moodEntry);

    void deleteById(MoodEntryId id);
}
