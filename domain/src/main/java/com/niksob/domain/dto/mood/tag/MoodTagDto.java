package com.niksob.domain.dto.mood.tag;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoodTagDto {
    private String name;
    private int degree;
}
