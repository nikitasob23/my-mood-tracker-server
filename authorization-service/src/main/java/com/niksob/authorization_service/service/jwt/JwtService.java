package com.niksob.authorization_service.service.jwt;

import com.niksob.authorization_service.model.jwt.Jwt;
import com.niksob.authorization_service.model.jwt.JwtDetails;
import io.jsonwebtoken.Claims;
import lombok.NonNull;

public interface JwtService {

    Jwt generate(JwtDetails jwtDetails);

    boolean validate(Jwt jwt);

    Claims getClaims(@NonNull Jwt jwt);

    JwtDetails getJwtDetails(@NonNull Jwt jwt);
}
