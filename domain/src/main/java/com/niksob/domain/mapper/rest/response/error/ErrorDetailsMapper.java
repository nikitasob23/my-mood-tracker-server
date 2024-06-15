package com.niksob.domain.mapper.rest.response.error;

import com.niksob.domain.exception.rest.controller.response.HttpClientException;
import com.niksob.domain.model.rest.response.error.ErrorDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ErrorDetailsMapper {
    @Mapping(target = "status", expression = "java(ex.getHttpStatus().value())")
    @Mapping(source = "httpStatus.reasonPhrase", target = "error")
    ErrorDetails fromResponseStatusException(HttpClientException ex);
}
