package com.niksob.domain.model.auth.login.active_code;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ActiveCode implements Serializable {
    private final String value;
}
