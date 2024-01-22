package com.niksob.database_service.entity.mood.tag;

import com.niksob.database_service.entity.mood.entry.MoodEntryEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "mood_tags")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoodTagEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(unique = true)
    private String name;

    private int degree;

    @ManyToOne
    @JoinColumn(name = "mood_entry_id")
    private MoodEntryEntity moodEntry;
}
