package com.niksob.domain.dto.auth.token;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthTokenDto {
    private String id;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("access_token")
    private String access;
    @JsonProperty("refresh_token")
    private String refresh;
}
