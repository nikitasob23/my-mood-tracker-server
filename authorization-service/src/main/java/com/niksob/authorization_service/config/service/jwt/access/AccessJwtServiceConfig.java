package com.niksob.authorization_service.config.service.jwt.access;

import com.niksob.authorization_service.mapper.jwt.params.claims.JwtDetailsMapper;
import com.niksob.authorization_service.service.jwt.JwtService;
import com.niksob.authorization_service.service.jwt.JwtServiceImpl;
import com.niksob.authorization_service.util.date.expiration.ExpirationDateUtil;
import com.niksob.authorization_service.util.key.decoder.SecretKeyDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
@RequiredArgsConstructor
public class AccessJwtServiceConfig {
    @Qualifier("accessTokenExpirationDateUtil")
    private final ExpirationDateUtil expirationDateUtil;

    @Value("${auth.token.secret-key.access}")
    private String secretKeyValue;

    @Bean("accessJwtService")
    public JwtService getAccessJwtService(JwtDetailsMapper jwtDetailsMapper) {
        final SecretKey secretKey = SecretKeyDecoder.decodeKey(secretKeyValue);
        return new JwtServiceImpl(secretKey, expirationDateUtil, jwtDetailsMapper);
    }
}
