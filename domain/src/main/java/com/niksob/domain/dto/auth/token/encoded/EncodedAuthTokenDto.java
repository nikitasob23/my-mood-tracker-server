package com.niksob.domain.dto.auth.token.encoded;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EncodedAuthTokenDto {
    private String id;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("encoded_access_token")
    private String access;
    @JsonProperty("encoded_refresh_token")
    private String refresh;
    private String device;
}
