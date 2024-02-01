package com.niksob.database_service.repository.mood.tag;

import com.niksob.database_service.entity.mood.tag.MoodTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MoodTagRepository extends JpaRepository<MoodTagEntity, Long> {
    boolean existsByName(String name);

    MoodTagEntity getByName(String name);

    @Modifying
    @Query(
            value = "INSERT INTO mood_tags (name, degree, user_id) VALUES (:name, :degree, :userId)",
            nativeQuery = true
    )
    void save(@Param("name") String name, @Param("degree") int degree, @Param("userId") Long userId);
}
