package com.niksob.logger.mapper.masked.json;

import com.google.gson.Gson;
import com.niksob.logger.MainContextTest;
import com.niksob.logger.mapper.json.AppJsonMapper;
import com.niksob.logger.model.*;
import com.niksob.logger.model.json.Json;
import com.niksob.logger.service.masking.MaskingStringFieldService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class MaskedFieldJsonMapperTest extends MainContextTest {
    private static final String MASK = "*****";
    private final String testUsernameValue = "TEST_USERNAME";
    private final String testPasswordValue = "TEST_PASSWORD";
    private final String testRefreshTokenValue = "TEST_REFRESH_TOKEN";
    private final TestStringUser stringUser = new TestStringUser(
            testUsernameValue, testPasswordValue, testRefreshTokenValue
    );
    private final TestObjectUser objectUser = new TestObjectUser(
            new TestUsername(testUsernameValue),
            new TestPassword(testPasswordValue),
            new TestRefreshToken(testRefreshTokenValue)
    );
    private final TestStringUser expectedStringUser = new TestStringUser(
            testUsernameValue, MASK, MASK
    );

    @Autowired
    private MaskingStringFieldService maskingStringFieldService;
    @Autowired
    private AppJsonMapper jsonMapper;
    @Autowired
    private Gson gson;

    @Test
    public void testJsonMaskingWithStringFields() {

        final Json objectJson = Stream.of(stringUser)
                .map(Object::toString)
                .map(Json::new)
                .map(maskingStringFieldService::mask)
                .findFirst().get();

        assertThat(objectJson.value()).isEqualTo(expectedStringUser.toString());
    }

    @Test
    public void testJsonMaskingWithObjectFields() {
        Stream.of(objectUser)
                .map(jsonMapper::toJson)
                .map(maskingStringFieldService::mask)
                .forEach(masked ->
                        assertThat(masked.value()).isEqualTo(
                                "{\"TestObjectUser\":" +
                                        "{\"username\":{\"value\":\"TEST_USERNAME\"}," +
                                        "\"password\":\"*****\"," +
                                        "\"refreshToken\":\"*****\"}" + "}"
                        )
                );
    }
}
