package com.niksob.domain.dto.mood.entry;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoodEntriesDetailsDto {
    private Long id;
    @JsonProperty("user_id")
    private Long userId;
    private int degree;
    @JsonProperty("date_time")
    private LocalDateTime dateTime;
    @JsonProperty("mood_tag_ids")
    private Set<String> moodTagIds = new HashSet<>();
}
