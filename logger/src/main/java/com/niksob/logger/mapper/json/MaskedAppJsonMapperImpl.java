package com.niksob.logger.mapper.json;

import com.google.gson.Gson;
import com.niksob.logger.model.json.Json;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@AllArgsConstructor
public class MaskedAppJsonMapperImpl implements AppJsonMapper {
    private final Gson gson;

    @Override
    public Json toJson(Object... objects) {
        String jsonStr = Arrays.stream(objects)
                .map(this::toJsonWithClassName)
                .reduce((str1, str2) -> str1 + "," + str2)
                .orElseThrow(() -> new IllegalStateException("ObjectStates value masking failed"));
        return new Json(jsonStr);
    }

    private String toJsonWithClassName(Object o) {
        String objectName = o.getClass().getSimpleName();
        return String.format("{\"%s\":%s}", objectName, gson.toJson(o));
    }
}
