package com.niksob.domain.mapper.dto.mood.entry;

import com.niksob.domain.dto.mood.entry.MoodEntriesDetailsDto;
import com.niksob.domain.model.mood.entry.MoodEntry;
import com.niksob.domain.model.mood.tag.MoodTag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface MoodEntriesDetailsDtoMapper {
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "userId.value", target = "userId")
    @Mapping(source = "degree.value", target = "degree")
    @Mapping(target = "moodTagIds", expression = "java(toMoodTagIds(moodEntries))")
    MoodEntriesDetailsDto toDetailsDto(MoodEntry moodEntries);

    @Mapping(source = "entryDetails.id", target = "id.value")
    @Mapping(source = "entryDetails.userId", target = "userId.value")
    @Mapping(source = "entryDetails.degree", target = "degree.value")
    @Mapping(source = "moodTagSet", target = "moodTags")
    MoodEntry fromDetailsDto(MoodEntriesDetailsDto entryDetails, Set<MoodTag> moodTagSet);

    default Set<String> toMoodTagIds(MoodEntry moodEntries) {
        return moodEntries.getMoodTags().stream()
                .map(tag -> tag.getId().getValue().toString())
                .collect(Collectors.toSet());
    }
}
