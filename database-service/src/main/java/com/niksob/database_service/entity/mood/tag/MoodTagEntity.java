package com.niksob.database_service.entity.mood.tag;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.niksob.database_service.entity.mood.entry.MoodEntryEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "mood_tags")
@Data
@ToString(exclude = "moodEntries")
@EqualsAndHashCode(exclude = "moodEntries")
@AllArgsConstructor
@NoArgsConstructor
public class MoodTagEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private int degree;

    @ManyToMany(mappedBy = "moodTags", cascade = {CascadeType.ALL})
    @JsonBackReference
    private Set<MoodEntryEntity> moodEntries = new HashSet<>();

    public MoodTagEntity(MoodTagEntity moodTag) {
        this.id = moodTag.getId();
        this.name = moodTag.getName();
        this.degree = moodTag.getDegree();
        this.moodEntries = new HashSet<>(moodTag.getMoodEntries());
    }

    public void addMoodEntry(MoodEntryEntity moodEntry) {
        moodEntries.add(moodEntry);
    }
}
