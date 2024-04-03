package com.niksob.authorization_service.service.jwt;

import com.niksob.authorization_service.exception.jwt.InvalidJwtException;
import com.niksob.authorization_service.exception.jwt.JwtDataReceivingException;
import com.niksob.authorization_service.exception.jwt.JwtGenerationException;
import com.niksob.authorization_service.mapper.jwt.params.claims.JwtDetailsMapper;
import com.niksob.authorization_service.model.jwt.Jwt;
import com.niksob.authorization_service.model.jwt.JwtDetails;
import com.niksob.authorization_service.util.date.expiration.ExpirationDateUtil;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import javax.crypto.SecretKey;

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
                    .setSubject(jwtDetails.getUsername())
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
        } catch (SignatureException e) {
            final String message = "Jwt token is not valid";
            log.error(message, null, jwt);
            throw new InvalidJwtException(message, e);
        } catch (Exception e) {
            final String message = "Unknown jwt exception throws";
            log.error(message, null, jwt);
            throw new JwtDataReceivingException(message, e);
        }
    }

    @Override
    public JwtDetails getJwtDetails(@NonNull Jwt jwt) {
        final Claims claims = getClaims(jwt);
        return jwtDetailsMapper.fromClaims(claims);
    }

    private void setJwtBuilderClaims(JwtBuilder jwtBuilder, JwtDetails jwtDetails) {
        jwtDetailsMapper.toClaimsMap(jwtDetails)
                .forEach(jwtBuilder::claim);
    }
}
