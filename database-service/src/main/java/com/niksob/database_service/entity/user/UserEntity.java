package com.niksob.database_service.entity.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.niksob.database_service.entity.mood.entry.MoodEntryEntity;
import com.niksob.database_service.entity.mood.tag.MoodTagEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "usrs")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonBackReference
    private Set<MoodEntryEntity> moodEntries = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonBackReference
    private Set<MoodTagEntity> moodTags = new HashSet<>();

    public UserEntity(Long id) {
        this.id = id;
    }

    public void setMoodEntries(Set<MoodEntryEntity> moodEntries) {
        this.moodEntries = moodEntries.stream()
                .peek(moodEntry -> moodEntry.setUser(this))
                .collect(Collectors.toSet());
    }

    public void setMoodTags(Set<MoodTagEntity> moodTags) {
        this.moodTags = moodTags.stream()
                .peek(moodTag -> moodTag.setUser(this))
                .collect(Collectors.toSet());
    }
}
