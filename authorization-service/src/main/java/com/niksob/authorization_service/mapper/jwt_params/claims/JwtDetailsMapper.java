package com.niksob.authorization_service.mapper.jwt_params.claims;

import com.niksob.authorization_service.model.jwt.JwtDetails;
import com.niksob.domain.model.user.UserInfo;
import io.jsonwebtoken.Claims;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = JwtClaimsMapper.class)
public interface JwtDetailsMapper {
    JwtDetails fromClaims(Claims claims);


    @Mapping(source = "username.value", target = "subject")
    JwtDetails fromUserInfo(UserInfo userInfo);

//    private JwtClaims generateClaims(Set<Role> roles) {
//        return new JwtClaims(
//                Map.of(rolesKey.name(), roles.stream()
//                        .map(Role::name)
//                        .collect(Collectors.toSet()))
//        );
//    }
}
