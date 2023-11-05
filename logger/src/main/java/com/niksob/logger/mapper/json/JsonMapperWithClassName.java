package com.niksob.logger.mapper.json;

import com.google.gson.Gson;
import com.niksob.logger.config.json.masked_fields.MaskedFieldsConfig;

public class JsonMapperWithClassName extends MaskedFieldJsonMapper {


    public JsonMapperWithClassName(Gson gson, MaskedFieldsConfig maskedFieldsConfig) {
        super(gson, maskedFieldsConfig);
    }

    @Override
    public String toJson(Object src) {
        return String.format("\"%s\": %s", src.getClass().getSimpleName(), super.toJson(src));
    }
}
