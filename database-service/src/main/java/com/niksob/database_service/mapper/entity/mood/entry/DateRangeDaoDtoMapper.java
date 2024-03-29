package com.niksob.database_service.mapper.entity.mood.entry;

import com.niksob.database_service.model.mood.entry.date.UserEntryDateRangeDaoDto;
import com.niksob.domain.model.mood.entry.UserEntryDateRange;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Mapper(componentModel = "spring")
public interface DateRangeDaoDtoMapper {
    @Mapping(source = "userId.value", target = "userId")
    @Mapping(target = "startDateTime", expression = "java(dateRange.getStartDate().atStartOfDay())")
    @Mapping(target = "endDateTime", expression = "java(getEndOfDay(dateRange.getEndDate()))")
    UserEntryDateRangeDaoDto toDaoDto(UserEntryDateRange dateRange);

    default LocalDateTime getEndOfDay(LocalDate localDate) {
        return localDate.atTime(LocalTime.MAX);
    }
}
