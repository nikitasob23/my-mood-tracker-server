package com.niksob.database_service.entity.mood.entry;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.niksob.database_service.entity.mood.tag.MoodTagEntity;
import com.niksob.database_service.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "mood_entries")
@Data
@ToString(exclude = "user")
@EqualsAndHashCode(exclude = "user")
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
    @JsonManagedReference
    private UserEntity user;

    @OneToMany(mappedBy = "moodEntry",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @JsonBackReference
    private Set<MoodTagEntity> moodTags = new HashSet<>();

    public void setMoodTags(Set<MoodTagEntity> moodTags) {
        this.moodTags = moodTags.stream()
                .peek(moodTag -> moodTag.setMoodEntry(this))
                .collect(Collectors.toSet());
    }
}
