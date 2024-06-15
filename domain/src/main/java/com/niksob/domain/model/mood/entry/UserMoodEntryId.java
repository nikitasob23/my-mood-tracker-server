package com.niksob.domain.model.mood.entry;

import com.niksob.domain.model.user.UserId;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserMoodEntryId {
    private final MoodEntryId id;
    private final UserId userId;
}
