package com.niksob.mapping_wrapper.config.model.class_details;

import com.niksob.mapping_wrapper.model.class_details.ClassDetails;
import com.niksob.mapping_wrapper.model.method_details.MethodSignature;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@NoArgsConstructor
@Getter
public class MapperClassDetailsTestConfig {
    public ClassDetails getDetails() {
        return new ClassDetails(
                "com.niksob.mapping_wrapper.mapper.user.UserEntityMapper",
                Set.of(
                        new MethodSignature("toEntityUsername", "java.lang.String", "com.niksob.domain.model.user.Username"),
                        new MethodSignature("toEntityUsername", "com.niksob.domain.model.user.Username", "java.lang.String"),
                        new MethodSignature("toEntity", "com.niksob.mapping_wrapper.entity.user.UserEntity", "com.niksob.domain.model.user.UserInfo"),
                        new MethodSignature("fromEntity", "com.niksob.domain.model.user.UserInfo", "com.niksob.mapping_wrapper.entity.user.UserEntity")
                ));
    }

    public ClassDetails getIncompleteDetails() {
        return new ClassDetails(
                "com.niksob.mapping_wrapper.mapper.user.UserEntityMapper",
                Set.of(new MethodSignature("toEntityUsername", "java.lang.String", "com.niksob.domain.model.user.Username"))
        );
    }

    public ClassDetails getDetailsWithIdenticalReturnTypes() {
        return new ClassDetails(
                "com.niksob.mapping_wrapper.mapper.user.UserEntityMapper",
                Set.of(
                        new MethodSignature("toEntityUsername", "java.lang.String", "com.niksob.domain.model.user.Username"),
                        new MethodSignature("toEntityUsername2", "java.lang.String", "com.niksob.domain.model.user.Username"),
                        new MethodSignature("toEntityUsername", "com.niksob.domain.model.user.Username", "java.lang.String"),
                        new MethodSignature("toEntity", "com.niksob.mapping_wrapper.entity.user.UserEntity", "com.niksob.domain.model.user.UserInfo"),
                        new MethodSignature("fromEntity", "com.niksob.domain.model.user.UserInfo", "com.niksob.mapping_wrapper.entity.user.UserEntity")
                ));
    }

    public ClassDetails getDetailsWithGeneric() {
        return new ClassDetails(
                "com.niksob.mapping_wrapper.mapper.user.UserEntityMapper",
                Set.of(
                        new MethodSignature("toEntityUsername", "java.lang.String", "com.niksob.domain.model.user.Username"),
                        new MethodSignature("toEntityUsername", "com.niksob.domain.model.user.Username", "java.lang.String"),
                        new MethodSignature("toMonoEntity", "reactor.core.publisher.Mono<com.niksob.mapping_wrapper.entity.user.UserEntity>", "reactor.core.publisher.Mono<com.niksob.domain.model.user.UserInfo>"),
                        new MethodSignature("fromMonoEntity", "reactor.core.publisher.Mono<com.niksob.domain.model.user.UserInfo>", "reactor.core.publisher.Mono<com.niksob.mapping_wrapper.entity.user.UserEntity>"),
                        new MethodSignature("toEntity", "com.niksob.mapping_wrapper.entity.user.UserEntity", "com.niksob.domain.model.user.UserInfo"),
                        new MethodSignature("fromEntity", "com.niksob.domain.model.user.UserInfo", "com.niksob.mapping_wrapper.entity.user.UserEntity")
                ));
    }
}
