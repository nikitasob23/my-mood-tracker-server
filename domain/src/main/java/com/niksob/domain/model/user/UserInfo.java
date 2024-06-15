package com.niksob.domain.model.user;

import com.niksob.domain.annotation.Default;
import com.niksob.domain.model.mood.entry.MoodEntry;
import com.niksob.domain.model.mood.tag.MoodTag;
import lombok.Data;

import java.util.Set;

@Data
public class UserInfo {
    private final UserId id;
    private final Email email;
    private final Username username;
    private final Password password;
    private final Set<MoodEntry> moodEntries;
    private final Set<MoodTag> moodTags;

    public UserInfo(Email email, Username username, Password password) {
        this.id = null;
        this.email = email;
        this.username = username;
        this.password = password;
        this.moodEntries = null;
        this.moodTags = null;
    }

    @Default
    public UserInfo(
            UserId id,
            Email email,
            Username username,
            Password password,
            Set<MoodEntry> moodEntries,
            Set<MoodTag> moodTags
    ) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.moodEntries = moodEntries;
        this.moodTags = moodTags;
    }

    public UserInfo(
            UserInfo userInfo,
            Set<MoodEntry> moodEntries,
            Set<MoodTag> moodTags
    ) {
        this.id = userInfo.getId();
        this.email = userInfo.getEmail();
        this.username = userInfo.getUsername();
        this.password = userInfo.getPassword();
        this.moodEntries = moodEntries;
        this.moodTags = moodTags;
    }

    public UserInfo(
            UserInfo userInfo,
            Set<MoodEntry> moodEntries
    ) {
        this.id = userInfo.getId();
        this.email = userInfo.getEmail();
        this.username = userInfo.getUsername();
        this.password = userInfo.getPassword();
        this.moodEntries = moodEntries;
        this.moodTags = userInfo.getMoodTags();
    }

    public UserInfo(UserInfo old, Password password) {
        this.id = old.getId();
        this.email = old.getEmail();
        this.username = old.getUsername();
        this.password = password;
        this.moodEntries = old.getMoodEntries();
        this.moodTags = old.getMoodTags();
    }

    public UserInfo(UserInfo old, Email email) {
        this.id = old.getId();
        this.email = email;
        this.username = old.getUsername();
        this.password = old.getPassword();
        this.moodEntries = old.getMoodEntries();
        this.moodTags = old.getMoodTags();
    }
}
