package com.niksob.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecurityUserDetailsDto {
    private Long id;
    private String username;
    private String nickname;
    private String password;
}