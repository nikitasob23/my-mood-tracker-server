package com.niksob.database_service.entity.mood.entry;

import com.niksob.database_service.entity.mood.tag.MoodTagEntity;
import com.niksob.database_service.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "mood_entries")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoodEntryEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int degree;

    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(
            mappedBy = "moodEntry",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH},
            fetch = FetchType.EAGER
    )
    private Set<MoodTagEntity> moodTags;
}
