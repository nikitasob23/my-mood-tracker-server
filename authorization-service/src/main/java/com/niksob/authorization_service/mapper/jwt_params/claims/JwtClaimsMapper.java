package com.niksob.authorization_service.mapper.jwt_params.claims;

import com.niksob.authorization_service.model.jwt.JwtClaims;
import io.jsonwebtoken.Claims;
import org.mapstruct.Mapper;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mapper(componentModel = "spring")
public class JwtClaimsMapper {
    private static final String SUBJECT_JWT_KEY = "sub";
    private static final String EXPIRATION_JWT_KEY = "exp";

    public JwtClaims fromClaims(Claims claims) {
        final Map<String, Object> claimMap = claims.entrySet().stream()
                .filter(this::excludeSubjectKey)
                .filter(this::excludeExpirationKey)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return new JwtClaims(claimMap);
    }

    private boolean excludeSubjectKey(Map.Entry<String, Object> claimEntry) {
        return Stream.of(claimEntry)
                .map(Map.Entry::getKey)
                .noneMatch(SUBJECT_JWT_KEY::equals);
    }

    private boolean excludeExpirationKey(Map.Entry<String, Object> claimEntry) {
        return Stream.of(claimEntry)
                .map(Map.Entry::getKey)
                .noneMatch(EXPIRATION_JWT_KEY::equals);
    }
}
