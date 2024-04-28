package com.niksob.database_service.entity.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.niksob.database_service.entity.mood.entry.MoodEntryEntity;
import com.niksob.database_service.entity.mood.tag.MoodTagEntity;
import com.niksob.database_service.entity.auth.token.AuthTokenEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "usrs", uniqueConstraints = {
        @UniqueConstraint(name = "uk_usrs_email", columnNames = "email"),
        @UniqueConstraint(name = "uk_usrs_username", columnNames = "username")
})
@Data
@ToString(exclude = {"moodEntries", "moodTags", "authTokens"})
@EqualsAndHashCode(exclude = {"moodEntries", "moodTags", "authTokens"})
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String username;

    @JsonIgnore
    @Column(name = "encoded_password", nullable = false)
    private String encodedPassword;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonBackReference
    private Set<MoodEntryEntity> moodEntries;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonBackReference
    private Set<MoodTagEntity> moodTags;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonBackReference
    private Set<AuthTokenEntity> authTokens;
}
