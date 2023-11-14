package com.niksob.logger.config.service.masking;

import com.niksob.logger.config.json.masked_fields.MaskedFieldsConfig;
import com.niksob.logger.model.masking.MaskedPattern;
import com.niksob.logger.model.masking.StringType;
import com.niksob.logger.service.masking.MaskingStringFieldService;
import com.niksob.logger.service.masking.MaskingStringFieldServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@AllArgsConstructor
public class MaskingFieldServiceConfig {
    public static final String MASKED_JSON_OBJECT_PATTERN_TEMPLATE = "\\\"%s\\\":\\{.+?\\}";
    public static final String MASKED_JSON_STRING_PATTERN_TEMPLATE = "\\\"%s\\\":\\\".+?\\\"";
    public static final String MASKED_OBJECT_STRING_PATTERN_TEMPLATE = "%s=.+?(?=,|\\)$)";
    public static final String MASK = "*****";

    public static final String JSON_STRING_TO_REPLACE_TEMPLATE = "\"%s\":\"%s\"";
    public static final String OBJECT_STRING_TO_REPLACE_TEMPLATE = "%s=%s";

    private final MaskedFieldsConfig maskedFieldsConfig;

    @Bean
    public MaskingStringFieldService getMaskingStringFieldService() {
        final Set<MaskedPattern> MaskedPatterns = createReplacementPatterns();
        return new MaskingStringFieldServiceImpl(MaskedPatterns);
    }

    private Set<MaskedPattern> createReplacementPatterns() {

        return Stream.of(
                createReplacementPattern(
                        MASKED_JSON_STRING_PATTERN_TEMPLATE, JSON_STRING_TO_REPLACE_TEMPLATE, StringType.JSON),
                createReplacementPattern(
                        MASKED_JSON_OBJECT_PATTERN_TEMPLATE, JSON_STRING_TO_REPLACE_TEMPLATE, StringType.JSON),
                createReplacementPattern(
                        MASKED_OBJECT_STRING_PATTERN_TEMPLATE, OBJECT_STRING_TO_REPLACE_TEMPLATE, StringType.OBJECT)
        ).reduce(Stream::concat).get().collect(Collectors.toSet());
    }

    private Stream<MaskedPattern> createReplacementPattern(
            String maskedStringPatternTemplate, String replacementStringTemplate, StringType stringType
    ) {
        return maskedFieldsConfig.getFieldNames().stream()
                .map(keyword -> {
                    final String stringPattern = String.format(maskedStringPatternTemplate, keyword);
                    final Pattern pattern = Pattern.compile(stringPattern);
                    final String mask = String.format(replacementStringTemplate, keyword, MASK);

                    return new MaskedPattern(keyword, pattern, mask, stringType);
                });
    }
}