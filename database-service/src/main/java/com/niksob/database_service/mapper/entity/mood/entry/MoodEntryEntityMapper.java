package com.niksob.database_service.mapper.entity.mood.entry;

import com.niksob.database_service.entity.mood.entry.MoodEntryEntity;
import com.niksob.database_service.mapper.entity.mood.tag.MoodTagEntityMapper;
import com.niksob.domain.model.mood.entry.MoodEntry;
import com.niksob.domain.model.mood.entry.MoodEntryId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {MoodTagEntityMapper.class})
public interface MoodEntryEntityMapper {
    default Long toEntityId(MoodEntryId id) {
        return id.getValue();
    }

    default Set<MoodEntry> fromEntitySet(Set<MoodEntryEntity> entities) {
        return entities.stream()
                .map(this::fromEntity)
                .collect(Collectors.toSet());
    }

    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "userId.value", target = "userId")
    @Mapping(source = "degree.value", target = "degree")
    MoodEntryEntity toEntity(MoodEntry moodEntry);

    @Mapping(source = "id", target = "id.value")
    @Mapping(source = "userId", target = "userId.value")
    @Mapping(source = "degree", target = "degree.value")
    MoodEntry fromEntity(MoodEntryEntity entity);
}
