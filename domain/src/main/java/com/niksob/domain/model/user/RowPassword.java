package com.niksob.domain.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class RowPassword implements Serializable {
    private final String value;
}
