package com.niksob.domain.dto.auth.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RowLoginInDetailsDto {
    private String username;
    @JsonProperty("password")
    private String rowPassword;
}
