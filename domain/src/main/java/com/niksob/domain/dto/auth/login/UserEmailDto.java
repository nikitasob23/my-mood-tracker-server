package com.niksob.domain.dto.auth.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEmailDto {
    @JsonProperty("user_id")
    private Long userId;
    private String email;
}
