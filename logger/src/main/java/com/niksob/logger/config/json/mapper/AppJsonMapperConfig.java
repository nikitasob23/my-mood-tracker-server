package com.niksob.logger.config.json.mapper;

import com.google.gson.Gson;
import com.niksob.logger.config.json.masked_fields.MaskedFieldsConfig;
import com.niksob.logger.mapper.json.MaskedAppJsonMapper;
import com.niksob.logger.mapper.json.JsonMapperWithClassName;
import com.niksob.logger.mapper.json.MaskedFieldJsonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppJsonMapperConfig {
    @Bean("app_json_mapper")
    public MaskedAppJsonMapper getAppJsonMapper(Gson gson, MaskedFieldsConfig maskedFieldsConfig) {
        return new JsonMapperWithClassName(gson, maskedFieldsConfig);
    }

    @Bean("masked_field_json_mapper")
    public MaskedAppJsonMapper getMaskedFieldJsonMapperTest(Gson gson, MaskedFieldsConfig maskedFieldsConfig) {
        return new MaskedFieldJsonMapper(gson, maskedFieldsConfig);
    }
}
