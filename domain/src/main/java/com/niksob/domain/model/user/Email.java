package com.niksob.domain.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Email implements Serializable {
    private final String value;
}
