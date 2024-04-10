package com.niksob.database_service.dao.mood.tag.facade;

import com.niksob.database_service.entity.mood.tag.MoodTagEntity;

import java.util.Set;

public interface TagEntityDaoFacade {
    Set<MoodTagEntity> loadByUserId(Long userId);

    MoodTagEntity save(MoodTagEntity tag);

    void update(MoodTagEntity tag);

    Set<MoodTagEntity> mergeAll(Set<MoodTagEntity> tags);

    void deleteById(MoodTagEntity tag);

    void deleteAllByUserId(Long userId);
}
