package com.niksob.domain.dto.mood.tag;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoodTagDto {
    private Long id;
    private String name;
    private int degree;
    private Long userId;

    public MoodTagDto(Long id, Long userId) {
        this.id = id;
        this.userId = userId;
    }
}
