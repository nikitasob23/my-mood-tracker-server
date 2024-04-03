package com.niksob.domain.mapper.dto.auth.login;

import com.niksob.domain.model.auth.login.RowLoginInDetails;
import com.niksob.domain.dto.auth.login.RowLoginInDetailsDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RowLoginInDetailsDtoMapper {
    @Mapping(source = "username", target = "username.value")
    @Mapping(source = "rowPassword", target = "rowPassword.value")
    RowLoginInDetails fromDto(RowLoginInDetailsDto dto);

    @Mapping(source = "username.value", target = "username")
    @Mapping(source = "rowPassword.value", target = "rowPassword")
    RowLoginInDetailsDto toDto(RowLoginInDetails rowLoginInDetails);
}
