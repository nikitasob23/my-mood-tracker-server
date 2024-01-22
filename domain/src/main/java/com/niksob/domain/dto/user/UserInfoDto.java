package com.niksob.domain.dto.user;

import com.niksob.domain.dto.mood.entry.MoodEntryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {
    private String username;
    private String nickname;
    private String password;
    private Set<MoodEntryDto> moodEntries;
}
