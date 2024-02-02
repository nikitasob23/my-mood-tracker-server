package com.niksob.database_service.entity.mood.tag;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.niksob.database_service.entity.mood.entry.MoodEntryEntity;
import com.niksob.database_service.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "mood_tags")
@Data
@ToString(exclude = {"user", "moodEntries"})
@EqualsAndHashCode(exclude = {"user", "moodEntries"})
@AllArgsConstructor
@NoArgsConstructor
public class MoodTagEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    private int degree;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "user_id", nullable = false)
    @JsonManagedReference
    private UserEntity user;

    @ManyToMany(mappedBy = "moodTags", cascade = {CascadeType.ALL})
    @JsonBackReference
    private Set<MoodEntryEntity> moodEntries = new HashSet<>();

    public MoodTagEntity(Long id, String name, int degree, Long userId, Set<MoodEntryEntity> moodEntries) {
        this.id = id;
        this.name = name;
        this.degree = degree;
        this.user = new UserEntity(userId);
        this.moodEntries = moodEntries;
    }

    public MoodTagEntity(MoodTagEntity moodTag) {
        this.id = moodTag.getId();
        this.name = moodTag.getName();
        this.degree = moodTag.getDegree();
        this.user = moodTag.getUser();
        this.moodEntries = new HashSet<>(moodTag.getMoodEntries());
    }

    public void addMoodEntry(MoodEntryEntity moodEntry) {
        moodEntries.add(moodEntry);
    }
}
