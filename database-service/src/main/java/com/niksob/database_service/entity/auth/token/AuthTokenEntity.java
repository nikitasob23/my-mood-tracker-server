package com.niksob.database_service.entity.auth.token;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.niksob.database_service.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "encoded_auth_tokens", uniqueConstraints = @UniqueConstraint(
        name = "uk_auth_tokens_user_id_device", columnNames = {"user_id", "device"}))
@Data
@ToString(exclude = "user")
@EqualsAndHashCode(exclude = "user")
@AllArgsConstructor
@NoArgsConstructor
public class AuthTokenEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "access", nullable = false)
    private String encodedAccess;

    @Column(name = "refresh", nullable = false)
    private String encodedRefresh;

    @Column(nullable = false)
    private String device;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_auth_tokens_user_id"))
    @JsonManagedReference
    private UserEntity user;
}
