package com.niksob.domain.mapper.rest.mood.entry;

import com.niksob.domain.dto.mood.entry.UserEntryDateRangeDto;
import com.niksob.domain.dto.mood.entry.UserMoodEntryIdDto;
import org.mapstruct.Mapper;

import java.util.Map;

@Mapper(componentModel = "spring")
public interface MoodEntryGetParamsMapper {
    String USER_ID_PARAM_KEY = "user_id";
    String USER_ID_KEY = "userId";
    String START_DATE_PARAM_KEY = "start_date";
    String END_DATE_PARAM_KEY = "end_date";
    String ID_PARAM_KEY = "id";

    default Map<String, String> getHttpParams(UserEntryDateRangeDto userEntryDateRange) {
        return Map.of(
                USER_ID_PARAM_KEY, userEntryDateRange.getUserId().toString(),
                START_DATE_PARAM_KEY, userEntryDateRange.getStartDate().toString(),
                END_DATE_PARAM_KEY, userEntryDateRange.getEndDate().toString()
        );
    }

    default Map<String, String> getHttpParams(UserMoodEntryIdDto userEntryId) {
        return Map.of(
                ID_PARAM_KEY, userEntryId.getId().toString(),
                USER_ID_KEY, userEntryId.getUserId().toString()
        );
    }
}
