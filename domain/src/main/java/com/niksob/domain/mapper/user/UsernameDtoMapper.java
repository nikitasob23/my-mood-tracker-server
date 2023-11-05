package com.niksob.domain.mapper.user;

import com.niksob.domain.dto.user.UsernameDto;
import com.niksob.domain.model.user.Username;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsernameDtoMapper {
    UsernameDto toDto(Username username);

    Username fromDto(UsernameDto usernameDto);
}
