package com.niksob.database_service.util.date;

import com.niksob.domain.model.mood.entry.UserEntryDateRange;
import com.niksob.domain.model.user.UserId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DefUserDateRangeUtil {
    private final Integer defDateRangeDays;

    public DefUserDateRangeUtil(
            @Value("${service.loading.mood-entry.def-date-interval-days:30}") Integer defDateRangeDays
    ) {
        this.defDateRangeDays = defDateRangeDays;
    }

    public UserEntryDateRange create(UserId userId) {
        final LocalDate now = LocalDate.now();
        return new UserEntryDateRange(userId, now.minusDays(defDateRangeDays), now);
    }
}
