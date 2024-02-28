package com.niksob.authorization_service.service.jwt;

import com.niksob.authorization_service.exception.auth.token.AuthTokenException;
import com.niksob.authorization_service.exception.auth.token.expired.ExpiredAuthTokenException;
import com.niksob.authorization_service.mapper.jwt_params.claims.JwtDetailsMapper;
import com.niksob.authorization_service.util.date.expiration.ExpirationDateUtil;
import com.niksob.authorization_service.model.jwt.Jwt;
import com.niksob.authorization_service.model.jwt.JwtDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import com.niksob.authorization_service.model.jwt.JwtClaims;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.stream.Stream;

@AllArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final SecretKey secretKey;

    private final Date expiatedDate;

    private JwtDetailsMapper jwtDetailsMapper;

    @Override
    public Jwt generate(@NonNull JwtDetails jwtDetails) {
        final JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject(jwtDetails.getSubject())
                .setExpiration(expiatedDate)
                .signWith(secretKey);
        setJwtBuilderClaims(jwtBuilder, jwtDetails);

        final String jwtValue = jwtBuilder.compact();
        return new Jwt(jwtValue);
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
