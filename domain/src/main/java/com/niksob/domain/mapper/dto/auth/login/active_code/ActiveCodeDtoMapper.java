package com.niksob.domain.mapper.dto.auth.login.active_code;

import com.niksob.domain.dto.auth.login.active_code.ActiveCodeDto;
import com.niksob.domain.model.auth.login.active_code.ActiveCode;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class ActiveCodeDtoMapper {
    public ActiveCode fromStrDto(String activeCodeStr) {
        return new ActiveCode(activeCodeStr);
    }

    public String toStrDto(ActiveCode activeCode) {
        return activeCode.getValue();
    }

    public abstract ActiveCode fromDto(ActiveCodeDto dto);

    public abstract ActiveCodeDto toDto(ActiveCode activeCode);
}
