package com.niksob.domain.dto.auth.login;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RowLoginInDetailsDto {
    private String username;
    private String rowPassword;
}
