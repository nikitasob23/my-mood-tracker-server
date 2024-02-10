package com.niksob.database_service.repository.mood.tag;

import com.niksob.database_service.entity.mood.tag.MoodTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface MoodTagRepository extends JpaRepository<MoodTagEntity, Long> {
    Set<MoodTagEntity> getByUserId(Long userId);
}