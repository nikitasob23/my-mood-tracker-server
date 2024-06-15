package com.niksob.domain.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private String email;
    private String username;
    private String password;
    @JsonProperty("mood_entries")
    private Set<MoodEntryDto> moodEntries = new HashSet<>();
    @JsonProperty("mood_tags")
    private Set<MoodTagDto> moodTags = new HashSet<>();

    public UserInfoDto(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }
}
