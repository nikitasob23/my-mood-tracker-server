package com.niksob.database_service.entity.mood.tag;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.niksob.database_service.entity.mood.entry.MoodEntryEntity;
import com.niksob.database_service.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "mood_tags", uniqueConstraints = @UniqueConstraint(
        name = "uk_mood_tags_unique_name_user_id", columnNames = {"name", "user_id"}))
@Data
@ToString(exclude = {"user", "moodEntries"})
@EqualsAndHashCode(exclude = {"user", "moodEntries"})
@AllArgsConstructor
@NoArgsConstructor
public class MoodTagEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int degree;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_mood_tags_user_id"))
    @JsonManagedReference
    private UserEntity user;

    @ManyToMany(mappedBy = "moodTags")
    @JsonBackReference
    private Set<MoodEntryEntity> moodEntries;
}
