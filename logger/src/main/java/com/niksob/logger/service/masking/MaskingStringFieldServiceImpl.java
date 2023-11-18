package com.niksob.logger.service.masking;

import com.niksob.logger.model.json.Json;
import com.niksob.logger.model.masking.MaskedPattern;
import lombok.AllArgsConstructor;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AllArgsConstructor
public class MaskingStringFieldServiceImpl implements MaskingStringFieldService {

    private final Set<MaskedPattern> maskedPatterns;

    @Override
    public Json mask(Json json) {
        String src = json.value();
        for (MaskedPattern maskedPattern : maskedPatterns) {
            src = mask(src, maskedPattern);
        }
        return new Json(src);
    }

    @Override
    public Json mask(Object o) {
        String src = o.toString();
        for (MaskedPattern maskedPattern : maskedPatterns) {
            src = mask(src, maskedPattern);
        }
        return new Json(src);
    }

    @Override
    public boolean haveKeywords(Object o) {
        return maskedPatterns.stream()
                .map(MaskedPattern::keyword)
                .anyMatch(o.toString()::contains);
    }

    @Override
    public boolean haveKeywords(Json json) {
        return maskedPatterns.stream()
                .map(MaskedPattern::keyword)
                .anyMatch(json.value()::contains);
    }

    private String mask(String src, MaskedPattern maskedPattern) {
        final Pattern pattern = maskedPattern.pattern();
        final Matcher matcher = pattern.matcher(src);
        while (matcher.find()) {
            String replaceable = matcher.group();
            String maskedValue = maskedPattern.mask();
            src = src.replace(replaceable, maskedValue);
        }
        return src;
    }
}
