package com.niksob.domain.mapper.dto.user.email;

import com.niksob.domain.dto.user.EmailDto;
import com.niksob.domain.model.user.Email;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmailDtoMapper {
    default String toStrDto(Email email) {
        return email.getValue();
    }

    EmailDto toDto(Email email);

    Email fromDto(EmailDto email);
}
