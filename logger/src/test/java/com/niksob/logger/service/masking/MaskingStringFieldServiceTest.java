package com.niksob.logger.service.masking;

import com.niksob.logger.config.service.masking.MaskingFieldServiceConfig;
import com.niksob.logger.model.json.Json;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MaskingStringFieldServiceTest {
    @Autowired
    private MaskingStringFieldService maskingStringFieldService;

    @Test
    public void testMask() {
        String jsonString = "{\"username\":{\"value\":\"TEST_USERNAME\"},\"nickname\":{\"value\":\"TEST_NICKNAME\"},\"password\":\"134Fckv5_-*y\",\"role\":\"USER\"}";
        String jsonObject = "{\"username\":{\"value\":\"TEST_USERNAME\"},\"nickname\":{\"value\":\"TEST_NICKNAME\"},\"password\":{\"value\":\"134Fkv5_-*y\"},\"role\":\"USER\"}";
        String objectString = "UserInfoDto(username=TEST_USERNAME, nickname=TEST_NICKNAME, password=Password(value=0000), refreshToken=1111)";

        String expectedJsonString = String.format("{\"username\":{\"value\":\"TEST_USERNAME\"},\"nickname\":{\"value\":\"TEST_NICKNAME\"},\"password\":\"%s\",\"role\":\"USER\"}", MaskingFieldServiceConfig.MASK);
        String expectedJsonObject = String.format("{\"username\":{\"value\":\"TEST_USERNAME\"},\"nickname\":{\"value\":\"TEST_NICKNAME\"},\"password\":\"%s\",\"role\":\"USER\"}", MaskingFieldServiceConfig.MASK);
        String expectedObjectString = String.format("UserInfoDto(username=TEST_USERNAME, nickname=TEST_NICKNAME, password=%s, refreshToken=%s)", MaskingFieldServiceConfig.MASK, MaskingFieldServiceConfig.MASK);

        final Json maskedJsonString = maskingStringFieldService.mask(new Json(jsonString));
        final Json maskedJsonObject = maskingStringFieldService.mask(new Json(jsonObject));
        final Json maskedObjectString = maskingStringFieldService.mask(new Json(objectString));

        assertThat(maskedJsonString.value()).isEqualTo(expectedJsonString);
        assertThat(maskedJsonObject.value()).isEqualTo(expectedJsonObject);
        assertThat(maskedObjectString.value()).isEqualTo(expectedObjectString);
    }
}
