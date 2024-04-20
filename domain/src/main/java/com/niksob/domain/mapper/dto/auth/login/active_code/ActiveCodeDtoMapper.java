package com.niksob.domain.mapper.dto.auth.login.active_code;

import com.niksob.domain.model.auth.login.active_code.ActiveCode;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ActiveCodeDtoMapper {
    default ActiveCode fromDto(String activeCodeStr) {
        return new ActiveCode(activeCodeStr);
    }
}
