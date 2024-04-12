package com.niksob.domain.mapper.dto.user.all;

import com.niksob.domain.dto.user.AllUserInfoDto;
import com.niksob.domain.mapper.dto.mood.entry.MoodEntriesDetailsDtoMapper;
import com.niksob.domain.mapper.dto.mood.tag.MoodTagDtoMapper;
import com.niksob.domain.model.user.UserInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import reactor.core.publisher.Mono;

@Mapper(componentModel = "spring", uses = {MoodEntriesDetailsDtoMapper.class, MoodTagDtoMapper.class})
public interface AllUserInfoDtoMapper {
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "username.value", target = "username")
    @Mapping(source = "nickname.value", target = "nickname")
    @Mapping(source = "password.value", target = "password")
    AllUserInfoDto toDto(UserInfo userInfo);

    default Mono<AllUserInfoDto> toMonoDto(Mono<UserInfo> mono) {
        return mono.map(this::toDto);
    }
}
