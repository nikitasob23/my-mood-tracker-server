package com.niksob.authorization_service.config.service.jwt.access;

import com.niksob.authorization_service.mapper.jwt_params.claims.JwtDetailsMapper;
import com.niksob.authorization_service.service.jwt.JwtService;
import com.niksob.authorization_service.service.jwt.JwtServiceImpl;
import com.niksob.authorization_service.util.date.expiration.ExpirationDateUtil;
import com.niksob.authorization_service.util.key.decoder.SecretKeyDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.util.Date;

@Configuration
@RequiredArgsConstructor
public class AccessJwtServiceConfig {
    private final ExpirationDateUtil expirationDateUtil;

    @Value("${auth.token.secret-key.access}")
    private String secretKeyValue;

    @Value("${auth.token.expiration-in-minutes.access}")
    private int expirationInMinutes;

    @Bean("accessJwtService")
    public JwtService getAccessJwtService(JwtDetailsMapper jwtDetailsMapper) {
        final SecretKey secretKey = SecretKeyDecoder.decodeKey(secretKeyValue);
        final Date expiratedDate = expirationDateUtil.getExpiratedDateByMinutes(expirationInMinutes);
        return new JwtServiceImpl(secretKey, expiratedDate, jwtDetailsMapper);
    }
}
