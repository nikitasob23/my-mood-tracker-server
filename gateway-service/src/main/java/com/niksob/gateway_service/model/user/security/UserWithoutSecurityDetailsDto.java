package com.niksob.gateway_service.model.user.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWithoutSecurityDetailsDto {
    private String username;
}
