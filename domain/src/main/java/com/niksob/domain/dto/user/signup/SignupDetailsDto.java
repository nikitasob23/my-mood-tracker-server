package com.niksob.domain.dto.user.signup;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignupDetailsDto {
    private String username;
    @JsonProperty("password")
    private String rowPassword;
}
