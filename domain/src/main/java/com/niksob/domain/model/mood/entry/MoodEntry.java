package com.niksob.domain.model.mood.entry;

import com.niksob.domain.model.mood.tag.MoodTag;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
public class MoodEntry {
    private final MoodEntryId id;
    private final MoodEntryDegree degree;
    private final LocalDateTime dateTime;
    private final Set<MoodTag> moodTags;
}
