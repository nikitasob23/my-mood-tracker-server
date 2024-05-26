package com.niksob.domain.mapper.mail;

import com.niksob.domain.dto.mood.active_code.ActiveCodeMailDetailsDto;
import com.niksob.domain.model.user.activation.ActivationUserDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ActiveCodeMailDetailsDtoMapper {
    @Mapping(source = "userDetails.username.value", target = "senderUsername")
    @Mapping(source = "email.value", target = "recipientEmail")
    @Mapping(source = "activeCode.value", target = "activeCode")
    ActiveCodeMailDetailsDto toDto(ActivationUserDetails activationUserDetails);
}
