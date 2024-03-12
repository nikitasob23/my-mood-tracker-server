package com.niksob.authorization_service.config.service.jwt;

import com.niksob.authorization_service.util.date.expiration.ExpirationDateUtil;
import com.niksob.authorization_service.util.date.expiration.ExpirationDateUtilImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExpirationDateUtilConfig {
    @Value("${auth.token.expiration-in-minutes.access}")
    private int accessExpiration;

    @Value("${auth.token.expiration-in-minutes.refresh}")
    private int refreshExpiration;

    @Bean("accessTokenExpirationDateUtil")
    public ExpirationDateUtil getAccessTokenExpirationDateUtil() {
        return new ExpirationDateUtilImpl(accessExpiration);
    }

    @Bean("refreshTokenExpirationDateUtil")
    public ExpirationDateUtil getRefreshTokenExpirationDateUtil() {
        return new ExpirationDateUtilImpl(refreshExpiration);
    }
}
