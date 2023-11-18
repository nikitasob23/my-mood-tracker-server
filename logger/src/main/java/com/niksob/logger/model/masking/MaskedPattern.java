package com.niksob.logger.model.masking;

import java.util.regex.Pattern;

public record MaskedPattern(
        String keyword,
        Pattern pattern,
        String mask,
        StringType type
) {
}
