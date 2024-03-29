package com.niksob.authorization_service.mapper.auth.login;

import com.niksob.domain.model.auth.login.SignOutDetails;
import com.niksob.domain.model.auth.token.details.AuthTokenDetails;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SignOutDetailsMapper {
    AuthTokenDetails toAuthTokenDetails(SignOutDetails signOutDetails);
}
