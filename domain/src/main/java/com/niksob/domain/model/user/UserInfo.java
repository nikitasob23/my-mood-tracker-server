package com.niksob.domain.model.user;

import com.niksob.domain.model.mood.entry.MoodEntry;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
public class UserInfo {
    private final Username username;
    private final Nickname nickname;
    private final Password password;
    private final Set<MoodEntry> moodEntries = new HashSet<>();
}
