package com.niksob.domain.model.mood.entry;

import com.niksob.domain.annotation.Default;
import com.niksob.domain.model.mood.tag.MoodTag;
import com.niksob.domain.model.user.UserId;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class MoodEntry {
    private final MoodEntryId id;
    private final UserId userId;
    private final MoodEntryDegree degree;
    private final LocalDateTime dateTime;
    private final Set<MoodTag> moodTags;

    @Default
    public MoodEntry(
            MoodEntryId id,
            UserId userId,
            MoodEntryDegree degree,
            LocalDateTime dateTime,
            Set<MoodTag> moodTags
    ) {
        this.id = id;
        this.userId = userId;
        this.degree = degree;
        this.dateTime = dateTime;
        this.moodTags = moodTags;
    }

    public MoodEntry(MoodEntry moodEntry, Set<MoodTag> moodTags) {
        this.id = moodEntry.getId();
        this.userId = moodEntry.getUserId();
        this.degree = moodEntry.getDegree();
        this.dateTime = moodEntry.getDateTime();
        this.moodTags = moodTags;
    }
}
