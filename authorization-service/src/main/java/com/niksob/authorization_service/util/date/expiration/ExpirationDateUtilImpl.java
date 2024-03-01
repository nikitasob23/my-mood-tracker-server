package com.niksob.authorization_service.util.date.expiration;

import lombok.AllArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@AllArgsConstructor
public class ExpirationDateUtilImpl implements ExpirationDateUtil {
    private final int expirationInMinutes;

    @Override
    public Date create() {
        final Instant dateInstant = LocalDateTime.now()
                .plusMinutes(expirationInMinutes)
                .atZone(ZoneId.systemDefault())
                .toInstant();
        return Date.from(dateInstant);
    }
}
