package com.niksob.domain.dto.auth.token.encoded.refresh;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenDto {
    @JsonProperty("refresh_token")
    private String refreshToken;
}
