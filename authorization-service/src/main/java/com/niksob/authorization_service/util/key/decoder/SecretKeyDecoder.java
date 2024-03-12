package com.niksob.authorization_service.util.key.decoder;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

public class SecretKeyDecoder {
    public static SecretKey decodeKey(String key) {
        return key == null ? null : Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
    }
}
