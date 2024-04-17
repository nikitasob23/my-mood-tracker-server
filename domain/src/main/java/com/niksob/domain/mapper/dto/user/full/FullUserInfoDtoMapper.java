package com.niksob.domain.mapper.dto.user.full;

import com.niksob.domain.dto.mood.tag.MoodTagDto;
import com.niksob.domain.dto.user.FullUserInfoDto;
import com.niksob.domain.mapper.dto.mood.entry.MoodEntriesDetailsDtoMapper;
import com.niksob.domain.mapper.dto.mood.tag.MoodTagDtoMapper;
import com.niksob.domain.model.mood.entry.MoodEntry;
import com.niksob.domain.model.mood.tag.MoodTag;
import com.niksob.domain.model.user.UserInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {MoodEntriesDetailsDtoMapper.class, MoodTagDtoMapper.class})
public abstract class FullUserInfoDtoMapper {
    @Autowired
    private MoodEntriesDetailsDtoMapper moodEntriesDetailsDtoMapper;
    @Autowired
    private MoodTagDtoMapper moodTagDtoMapper;

    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "username.value", target = "username")
    @Mapping(source = "nickname.value", target = "nickname")
    @Mapping(source = "password.value", target = "password")
    public abstract FullUserInfoDto toDto(UserInfo userInfo);

    @Mapping(source = "fullUserInfoDto.id", target = "id.value")
    @Mapping(source = "fullUserInfoDto.username", target = "username.value")
    @Mapping(source = "fullUserInfoDto.nickname", target = "nickname.value")
    @Mapping(source = "fullUserInfoDto.password", target = "password.value")
    @Mapping(source = "entrySet", target = "moodEntries")
    public abstract UserInfo getUserInfo(Set<MoodEntry> entrySet, FullUserInfoDto fullUserInfoDto);

    public UserInfo fromDto(FullUserInfoDto fullUserInfoDto) {
        final Set<MoodEntry> moodEntries = fullUserInfoDto.getMoodEntries().stream()
                .map(entryDetails -> moodEntriesDetailsDtoMapper.fromDetailsDto(
                        entryDetails,
                        mapMoodTags(entryDetails.getMoodTagIds(), fullUserInfoDto.getMoodTags())
                )).collect(Collectors.toSet());
        return getUserInfo(moodEntries, fullUserInfoDto);
    }

    public Mono<FullUserInfoDto> toMonoDto(Mono<UserInfo> mono) {
        return mono.map(this::toDto);
    }

    public Mono<UserInfo> fromMonoDto(Mono<FullUserInfoDto> mono) {
        return mono.map(this::fromDto);
    }

    private Set<MoodTag> mapMoodTags(Set<String> tagIds, Set<MoodTagDto> tagDtoSet) {
        return tagDtoSet.stream()
                .filter(tagDto -> tagIds.contains(tagDto.getId().toString()))
                .map(moodTagDtoMapper::fromDto)
                .collect(Collectors.toSet());
    }
}
