package com.niksob.domain.mapper.dto.mood.entry;

import com.niksob.domain.dto.mood.entry.UserEntryDateRangeDto;
import com.niksob.domain.model.mood.entry.UserEntryDateRange;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserEntryDateRangeDtoMapper {
    @Mapping(source = "userId", target = "userId.value")
    UserEntryDateRange toDaoDto(UserEntryDateRangeDto dateRange);

    @Mapping(source = "userId.value", target = "userId")
    UserEntryDateRangeDto fromDaoDto(UserEntryDateRange dateRange);
}
