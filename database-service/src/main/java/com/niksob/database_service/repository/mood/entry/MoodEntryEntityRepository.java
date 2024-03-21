package com.niksob.database_service.repository.mood.entry;

import com.niksob.database_service.model.mood.entry.date.UserEntryDateRangeDaoDto;
import com.niksob.database_service.entity.mood.entry.MoodEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface MoodEntryEntityRepository extends JpaRepository<MoodEntryEntity, Long> {
    @Query("SELECT entries FROM MoodEntryEntity entries WHERE (entries.userId = :#{#dateRange.userId} " +
            "AND (entries.dateTime BETWEEN :#{#dateRange.startDateTime} AND :#{#dateRange.endDateTime}))")
    Set<MoodEntryEntity> loadByDateRange(UserEntryDateRangeDaoDto dateRange);
}
