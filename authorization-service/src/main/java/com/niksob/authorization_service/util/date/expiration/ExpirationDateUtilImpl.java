package com.niksob.authorization_service.util.date.expiration;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.stream.Stream;

@Component
public class ExpirationDateUtilImpl implements ExpirationDateUtil {
    @Override
    public Date getExpiratedDateByMinutes(int minutes) {
        final Instant dateInstant = LocalDateTime.now()
                .plusMinutes(minutes)
                .atZone(ZoneId.systemDefault())
                .toInstant();
        return Date.from(dateInstant);
    }
}
