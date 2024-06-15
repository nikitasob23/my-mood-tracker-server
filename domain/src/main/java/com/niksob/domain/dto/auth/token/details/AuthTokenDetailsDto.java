package com.niksob.domain.dto.auth.token.details;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthTokenDetailsDto {
    private String username;
    private String userId;
    private String device;
}
