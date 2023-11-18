package com.niksob.logger.mapper.json;

import com.niksob.logger.model.json.Json;

public interface AppJsonMapper {
    Json toJson(Object... objects);
}
