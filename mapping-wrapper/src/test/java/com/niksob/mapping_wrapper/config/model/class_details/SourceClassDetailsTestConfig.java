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
public class SourceClassDetailsTestConfig {
    public ClassDetails getDetails() {
        return new ClassDetails(
                "com.niksob.mapping_wrapper.dao.user.UserEntityDaoImpl",
                Set.of(
                        new MethodSignature("load", "com.niksob.mapping_wrapper.entity.user.UserEntity", "java.lang.String"),
                        new MethodSignature("save", "void", "com.niksob.mapping_wrapper.entity.user.UserEntity"),
                        new MethodSignature("update", "com.niksob.mapping_wrapper.entity.user.UserEntity", "com.niksob.mapping_wrapper.entity.user.UserEntity"),
                        new MethodSignature("delete", "com.niksob.mapping_wrapper.entity.user.UserEntity", "java.lang.String"),
                        new MethodSignature("getCurrentUser", "com.niksob.mapping_wrapper.entity.user.UserEntity", null),
                        new MethodSignature("clearCache", "void", null),
                        new MethodSignature("get", "java.lang.String", "java.lang.String"),
                        new MethodSignature("createEntityNotFoundException", "RuntimeException", "java.lang.String")
                ));
    }

    public ClassDetails getDetailsWithInsufficientMethods() {
        return new ClassDetails(
                "com.niksob.mapping_wrapper.dao.user.UserEntityDaoImpl",
                Set.of(new MethodSignature("getValue", "java.lang.String", null))
        );
    }

    public ClassDetails getDetailsWithGeneric() {
        return new ClassDetails(
                "com.niksob.mapping_wrapper.dao.user.UserEntityDaoImpl",
                Set.of(
                        new MethodSignature("load", "reactor.core.publisher.Mono<com.niksob.mapping_wrapper.entity.user.UserEntity>", "java.lang.String"),
                        new MethodSignature("save", "void", "com.niksob.mapping_wrapper.entity.user.UserEntity"),
                        new MethodSignature("update", "reactor.core.publisher.Mono<com.niksob.mapping_wrapper.entity.user.UserEntity>", "com.niksob.mapping_wrapper.entity.user.UserEntity"),
                        new MethodSignature("delete", "reactor.core.publisher.Mono<com.niksob.mapping_wrapper.entity.user.UserEntity>", "java.lang.String"),
                        new MethodSignature("getCurrentUser", "reactor.core.publisher.Mono<com.niksob.mapping_wrapper.entity.user.UserEntity>", null),
                        new MethodSignature("clearCache", "void", null),
                        new MethodSignature("get", "java.lang.String", "java.lang.String"),
                        new MethodSignature("createEntityNotFoundException", "RuntimeException", "java.lang.String")
                ));
    }
}
