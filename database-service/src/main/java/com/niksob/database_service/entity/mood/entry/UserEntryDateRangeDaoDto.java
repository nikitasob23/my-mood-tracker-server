package com.niksob.database_service.entity.mood.entry;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserEntryDateRangeDaoDto {
    private final Long userId;
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;
}
