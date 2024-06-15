package com.niksob.domain.dto.auth.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserPasswordPairDto {
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("old_password")
    private String oldRowPassword;
    @JsonProperty("new_password")
    private String newRowPassword;
}
