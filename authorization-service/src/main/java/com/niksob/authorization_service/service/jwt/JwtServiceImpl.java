package com.niksob.authorization_service.service.jwt;

import com.niksob.authorization_service.exception.auth.token.AuthTokenException;
import com.niksob.authorization_service.exception.auth.token.expired.ExpiredAuthTokenException;
import com.niksob.authorization_service.exception.jwt.JwtGenerationException;
import com.niksob.authorization_service.mapper.jwt_params.claims.JwtDetailsMapper;
import com.niksob.authorization_service.model.jwt.Jwt;
import com.niksob.authorization_service.model.jwt.JwtDetails;
import com.niksob.authorization_service.util.date.expiration.ExpirationDateUtil;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import com.niksob.authorization_service.model.jwt.JwtClaims;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

@AllArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final SecretKey secretKey;

    private final ExpirationDateUtil expirationDateUtil;

    private JwtDetailsMapper jwtDetailsMapper;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(JwtServiceImpl.class);

    @Override
    public Jwt generate(@NonNull JwtDetails jwtDetails) {
        try {
            final JwtBuilder jwtBuilder = Jwts.builder()
                    .setSubject(jwtDetails.getSubject())
                    .setExpiration(expirationDateUtil.create())
                    .signWith(secretKey);
            setJwtBuilderClaims(jwtBuilder, jwtDetails);

            final String jwtValue = jwtBuilder.compact();
            return new Jwt(jwtValue);
        } catch (Exception e) {
            final String failedMessage = "Jwt generation failed";
            log.error(failedMessage, null, jwtDetails);
            throw new JwtGenerationException(failedMessage, e);
        }
    }

    @Override
    public boolean validate(@NonNull Jwt jwt) {
        try {
            getClaims(jwt);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public Claims getClaims(@NonNull Jwt jwt) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(jwt.getValue())
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new ExpiredAuthTokenException("Token is expired", e);
        } catch (Exception e) {
            throw new AuthTokenException("Failed to get auth token claims", e);
        }
    }

    @Override
    public JwtDetails getJwtDetails(@NonNull Jwt jwt) {
        final Claims claims = getClaims(jwt);
        return jwtDetailsMapper.fromClaims(claims);
    }

    private void setJwtBuilderClaims(JwtBuilder jwtBuilder, JwtDetails jwtDetails) {
        Stream.of(jwtDetails.getClaims())
                .map(JwtClaims::getStorage)
                .map(Map::entrySet)
                .flatMap(Collection::stream)
                .forEach(entry -> jwtBuilder.claim(entry.getKey(), entry.getValue()));
    }
}
