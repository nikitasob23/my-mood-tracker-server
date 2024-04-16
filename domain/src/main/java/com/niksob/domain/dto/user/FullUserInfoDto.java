package com.niksob.domain.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.niksob.domain.dto.mood.entry.MoodEntriesDetailsDto;
import com.niksob.domain.dto.mood.tag.MoodTagDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullUserInfoDto {
    private Long id;
    private String username;
    private String nickname;
    private String password;
    @JsonProperty("mood_entries")
    private Set<MoodEntriesDetailsDto> moodEntries = new HashSet<>();
    @JsonProperty("mood_tags")
    private Set<MoodTagDto> moodTags = new HashSet<>();
}
