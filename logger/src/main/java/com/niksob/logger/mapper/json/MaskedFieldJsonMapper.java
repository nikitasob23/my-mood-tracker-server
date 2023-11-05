package com.niksob.logger.mapper.json;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.niksob.logger.config.json.masked_fields.MaskedFieldsConfig;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class MaskedFieldJsonMapper implements AppJsonMapper {

    protected final Gson gson;

    private static final String MASK = "*****";

    private final MaskedFieldsConfig maskedFieldsConfig;

    @Override
    public String toJson(Object src) {
        return Stream.of(src)
                .map(gson::toJson)
                .map(JSONObject::new)
                .flatMap(this::maskFields)
                .map(JSONObject::toString)
                .findFirst().orElseThrow(() -> new JsonParseException("Error during masking json for logger"));
    }

    private Stream<JSONObject> maskFields(JSONObject jsonObject) {
        return Stream.of(jsonObject)
                .map(this::filterMaskedFieldsStream).flatMap(Set::stream)
                .map(maskedField -> jsonObject.put(maskedField, MASK));
    }

    private Set<String> filterMaskedFieldsStream(JSONObject jsonObject) {
        return maskedFieldsConfig.getFieldNames().stream()
                .filter(jsonObject::has)
                .collect(Collectors.toSet());
    }
}
