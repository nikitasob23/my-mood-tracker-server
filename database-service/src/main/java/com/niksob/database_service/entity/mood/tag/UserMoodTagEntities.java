package com.niksob.database_service.entity.mood.tag;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

@Data
@AllArgsConstructor
public class UserMoodTagEntities implements Serializable {
    private final Long userId;
    private final Set<MoodTagEntity> tags;

    public void add(MoodTagEntity tag) {
        tags.add(tag);
    }

    public void remove(MoodTagEntity tag) {
        tags.remove(tag);
    }

    public void update(MoodTagEntity oldTag, MoodTagEntity newTag) {
        remove(oldTag);
        add(newTag);
    }

    public void addAll(Collection<MoodTagEntity> tags) {
        this.tags.addAll(tags);
    }

    public void removeAll(Collection<MoodTagEntity> tags) {
        this.tags.removeAll(tags);
    }

    public void updateAll(Collection<MoodTagEntity> oldTags, Collection<MoodTagEntity> newTags) {
        removeAll(oldTags);
        addAll(newTags);
    }
}
