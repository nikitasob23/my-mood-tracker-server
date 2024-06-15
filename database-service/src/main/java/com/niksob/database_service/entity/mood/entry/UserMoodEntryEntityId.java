package com.niksob.database_service.entity.mood.entry;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserMoodEntryEntityId {
    private final Long id;
    private final Long userId;
}
