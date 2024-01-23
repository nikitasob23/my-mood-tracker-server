package com.niksob.domain.model.mood.tag;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MoodTag {
    private final MoodTagId id;
    private final MoodTagName name;
    private final MoodTagDegree degree;
}
