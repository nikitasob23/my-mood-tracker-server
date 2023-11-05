package com.niksob.logger.service.masking;

import com.niksob.logger.model.masking.MaskedPattern;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public class MaskingStringFieldServiceImpl implements MaskingStringFieldService {

    private final Set<MaskedPattern> maskedPatterns;

//    @Override
//    public String mask(String src) {
//        return maskedPatterns.stream()
//                .flatMap(maskedPattern -> mask(src, maskedPattern))
//                .collect(Collectors.joining());
//    }
//
//    private Stream<String> mask(String src, MaskedPattern maskedPattern) {
//        return Stream.of(maskedPattern)
//                .map(MaskedPattern::pattern)
//                .map(pattern -> pattern.matcher(src))
//                .filter(Matcher::find)
//                .map(Matcher::group)
//                .map(replaceable -> src.replace(replaceable, maskedPattern.mask()));
//    }

    @Override
    public String mask(String src) {
        for (MaskedPattern maskedPattern : maskedPatterns) {
            src = mask(src, maskedPattern);
        }
        return src;
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
