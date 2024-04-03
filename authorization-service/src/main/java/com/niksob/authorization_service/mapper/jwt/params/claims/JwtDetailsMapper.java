package com.niksob.authorization_service.mapper.jwt.params.claims;

import com.niksob.authorization_service.model.jwt.JwtDetails;
import com.niksob.domain.model.auth.token.details.AuthTokenDetails;
import com.niksob.domain.model.user.UserId;
import io.jsonwebtoken.Claims;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Map;

@Mapper(componentModel = "spring")
public interface JwtDetailsMapper {
    String SUBJECT_KEY = "sub";
    String USER_ID_KEY = "userId";
    String DEVICE_KEY = "dev";

    @Mapping(source = "username.value", target = "username")
    @Mapping(target = "userId", expression = "java(toStringUserId(authTokenDetails))")
    JwtDetails toAuthTokenDetails(AuthTokenDetails authTokenDetails);

    @Mapping(source = "username", target = "username.value")
    @Mapping(target = "userId", expression = "java(toUserId(jwtDetails))")
    AuthTokenDetails toAuthTokenDetails(JwtDetails jwtDetails);

    default Map<String, Object> toClaimsMap(JwtDetails jwtDetails) {
        return Map.of(
                USER_ID_KEY, jwtDetails.getUserId(),
                DEVICE_KEY, jwtDetails.getDevice()
        );
    }

    default JwtDetails fromClaims(Claims claims) {
        final String username = (String) claims.get(SUBJECT_KEY);
        final String userId = (String) claims.get(USER_ID_KEY);
        final String device = (String) claims.get(DEVICE_KEY);
        return new JwtDetails(username, userId, device);
    }

    default String toStringUserId(AuthTokenDetails authTokenDetails) {
        final UserId userId = authTokenDetails.getUserId();
        final Long value = userId.getValue();
        final String string = value.toString();
        return string;
    }

    default UserId toUserId(JwtDetails jwtDetails) {

        final long value = Long.parseLong(jwtDetails.getUserId());
        return new UserId(value);
    }
}
