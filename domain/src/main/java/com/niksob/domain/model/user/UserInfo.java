package com.niksob.domain.model.user;

import com.niksob.domain.annotation.Default;
import com.niksob.domain.model.mood.entry.MoodEntry;
import com.niksob.domain.model.mood.tag.MoodTag;
import lombok.Data;

import java.util.Set;

@Data
public class UserInfo {
    private final UserId id;
    private final Username username;
    private final Nickname nickname;
    private final Password password;
    private final Set<MoodEntry> moodEntries;
    private final Set<MoodTag> moodTags;

    public UserInfo(Username username, Nickname nickname, Password password) {
        this.id = null;
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.moodEntries = null;
        this.moodTags = null;
    }

    @Default
    public UserInfo(
            UserId id,
            Username username,
            Nickname nickname,
            Password password,
            Set<MoodEntry> moodEntries,
            Set<MoodTag> moodTags
    ) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
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
        this.username = userInfo.getUsername();
        this.nickname = userInfo.getNickname();
        this.password = userInfo.getPassword();
        this.moodEntries = moodEntries;
        this.moodTags = moodTags;
    }
}
