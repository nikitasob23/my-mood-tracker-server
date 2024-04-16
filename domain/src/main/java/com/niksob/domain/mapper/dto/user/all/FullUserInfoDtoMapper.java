package com.niksob.domain.mapper.dto.user.all;

import com.niksob.domain.dto.user.FullUserInfoDto;
import com.niksob.domain.mapper.dto.mood.entry.MoodEntriesDetailsDtoMapper;
import com.niksob.domain.mapper.dto.mood.tag.MoodTagDtoMapper;
import com.niksob.domain.model.user.UserInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import reactor.core.publisher.Mono;

@Mapper(componentModel = "spring", uses = {MoodEntriesDetailsDtoMapper.class, MoodTagDtoMapper.class})
public interface FullUserInfoDtoMapper {
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "username.value", target = "username")
    @Mapping(source = "nickname.value", target = "nickname")
    @Mapping(source = "password.value", target = "password")
    FullUserInfoDto toDto(UserInfo userInfo);

    default Mono<FullUserInfoDto> toMonoDto(Mono<UserInfo> mono) {
        return mono.map(this::toDto);
    }
}
