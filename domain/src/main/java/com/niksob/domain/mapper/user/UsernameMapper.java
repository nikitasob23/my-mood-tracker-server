package com.niksob.domain.mapper.user;

import com.niksob.domain.model.user.Username;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsernameMapper {
    default Username toUsername(String value) {
        return new Username(value);
    }
}
