package com.niksob.domain.dto.mood.entry;

import com.niksob.domain.dto.mood.tag.MoodTagDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoodEntryDto {
    private int degree;
    private LocalDateTime dateTime;
    private Set<MoodTagDto> moodTags;
}
