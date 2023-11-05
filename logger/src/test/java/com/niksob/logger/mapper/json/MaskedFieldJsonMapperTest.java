package com.niksob.logger.mapper.json;

import com.google.gson.Gson;
import com.niksob.logger.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MaskedFieldJsonMapperTest {
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
    private final TestObjectUser expectedObjectUser = new TestObjectUser(
            new TestUsername(testUsernameValue),
            new TestPassword(MASK),
            new TestRefreshToken(MASK)
    );

    @Autowired
    @Qualifier("masked_field_json_mapper")
    private AppJsonMapper jsonMapper;

    @Autowired
    private Gson gson;

    @Test
    public void testJsonMaskingWithStringFields() {

        final TestStringUser resStringUser = Stream.of(stringUser)
                .map(jsonMapper::toJson)
                .map(json -> gson.fromJson(json, TestStringUser.class))
                .findFirst().get();

        assertThat(resStringUser).isEqualTo(expectedStringUser);
    }

    @Test
    public void testJsonMaskingWithObjectFields() {
        assertThat(
                jsonMapper.toJson(objectUser)
        ).isEqualTo( // the json that the method jsonMapper.toJson() returns does not involve a reverse conversion. If the field for masking is an object, then it will be completely replaced with a mask
                "{\"password\":\"*****\",\"username\":{\"value\":\"TEST_USERNAME\"},\"refreshToken\":\"*****\"}"
        );
    }
}
