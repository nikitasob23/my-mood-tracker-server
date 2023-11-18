package com.niksob.logger.service.masking;

import com.niksob.logger.model.json.Json;

public interface MaskingStringFieldService {
    Json mask(Json json);

    Json mask(Object o);

    boolean haveKeywords(Object o);

    boolean haveKeywords(Json json);
}
