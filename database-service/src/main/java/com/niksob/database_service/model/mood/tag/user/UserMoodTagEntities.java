package com.niksob.database_service.model.mood.tag.user;

import com.niksob.database_service.entity.mood.tag.MoodTagEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class UserMoodTagEntities implements Serializable {
    private final Long userId;
    private final Map<Long, MoodTagEntity> tags;

    public UserMoodTagEntities(Long userId, Set<MoodTagEntity> tags) {
        this.userId = userId;
        this.tags = tags.stream().collect(Collectors.toMap(MoodTagEntity::getId, tag -> tag));
    }

    public Set<MoodTagEntity> getTags() {
        return new HashSet<>(tags.values());
    }

    public void put(MoodTagEntity tag) {
        tags.put(tag.getId(), tag);
    }

    public void remove(MoodTagEntity tag) {
        tags.remove(tag.getId());
    }

    public void putAll(Collection<MoodTagEntity> tags) {
        tags.forEach(this::put);
    }

    public void removeAll(Collection<MoodTagEntity> tags) {
        tags.forEach(this::remove);
    }
}
