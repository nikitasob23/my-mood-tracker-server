package com.niksob.database_service.repository.mood.tag;

import com.niksob.database_service.entity.mood.tag.MoodTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoodTagRepository extends JpaRepository<MoodTagEntity, Long> {
    MoodTagEntity getByName(String name);

    void deleteMoodTagEntityByName(String name);
}
