package com.niksob.domain.model.mood.entry;

import com.niksob.domain.model.user.UserId;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UserEntryDateRange {
    private final UserId userId;
    private final LocalDate startDate;
    private final LocalDate endDate;
}
