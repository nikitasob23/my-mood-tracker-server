package com.niksob.domain.dto.mood.entry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMoodEntryIdDto {
    private Long id;
    private Long userId;
}
