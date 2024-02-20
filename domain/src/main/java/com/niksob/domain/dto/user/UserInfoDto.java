package com.niksob.domain.dto.user;

import com.niksob.domain.dto.mood.entry.MoodEntryDto;
import com.niksob.domain.dto.mood.tag.MoodTagDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {
    private Long id;
    private String username;
    private String nickname;
    private String password;
    private Set<MoodEntryDto> moodEntries = new HashSet<>();
    private Set<MoodTagDto> moodTags = new HashSet<>();

    public UserInfoDto(String username, String nickname, String password) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
    }
}
