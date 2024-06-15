package com.niksob.domain.mapper.rest.mood.tag;

import com.niksob.domain.dto.mood.tag.MoodTagDto;
import com.niksob.domain.dto.user.UserIdDto;
import org.mapstruct.Mapper;

import java.util.Map;

@Mapper(componentModel = "spring")
public interface MoodTagGetParamsMapper {
    String ID_PARAM_KEY = "id";
    String USER_ID_PARAM_KEY = "user_id";

    default Map<String, String> getHttpParams(UserIdDto userId) {
        return Map.of(USER_ID_PARAM_KEY, userId.getValue());
    }

    default Map<String, String> getHttpParams(MoodTagDto moodTag) {
        return Map.of(
                ID_PARAM_KEY, moodTag.getId().toString(),
                USER_ID_PARAM_KEY, moodTag.getUserId().toString()
        );
    }
}
