package com.niksob.domain.mapper.dto.user;

import com.niksob.domain.dto.user.UsernameDto;
import com.niksob.domain.model.user.Username;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsernameDtoMapper {
    Username fromDto(UsernameDto usernameDto);

    UsernameDto toDto(Username username);
}
