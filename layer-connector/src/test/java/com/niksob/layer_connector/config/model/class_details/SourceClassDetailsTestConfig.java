package com.niksob.layer_connector.config.model.class_details;

import com.niksob.layer_connector.model.class_details.ClassDetails;
import com.niksob.layer_connector.model.method_details.MethodSignature;
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
                "com.niksob.layer_connector.dao.user.UserEntityDaoImpl",
                Set.of(
                        new MethodSignature("load", "com.niksob.layer_connector.entity.user.UserEntity", "java.lang.String"),
                        new MethodSignature("save", "void", "com.niksob.layer_connector.entity.user.UserEntity"),
                        new MethodSignature("update", "com.niksob.layer_connector.entity.user.UserEntity", "com.niksob.layer_connector.entity.user.UserEntity"),
                        new MethodSignature("delete", "com.niksob.layer_connector.entity.user.UserEntity", "java.lang.String"),
                        new MethodSignature("getCurrentUser", "com.niksob.layer_connector.entity.user.UserEntity", null),
                        new MethodSignature("clearCache", "void", null),
                        new MethodSignature("get", "java.lang.String", "java.lang.String"),
                        new MethodSignature("createEntityNotFoundException", "RuntimeException", "java.lang.String")
                ));
    }

    public ClassDetails getDetailsWithInsufficientMethods() {
        return new ClassDetails(
                "com.niksob.layer_connector.dao.user.UserEntityDaoImpl",
                Set.of(new MethodSignature("getValue", "java.lang.String", null))
        );
    }

    public ClassDetails getDetailsWithGeneric() {
        return new ClassDetails(
                "com.niksob.layer_connector.dao.user.UserEntityDaoImpl",
                Set.of(
                        new MethodSignature("load", "reactor.core.publisher.Mono<com.niksob.layer_connector.entity.user.UserEntity>", "java.lang.String"),
                        new MethodSignature("save", "void", "com.niksob.layer_connector.entity.user.UserEntity"),
                        new MethodSignature("update", "reactor.core.publisher.Mono<com.niksob.layer_connector.entity.user.UserEntity>", "com.niksob.layer_connector.entity.user.UserEntity"),
                        new MethodSignature("delete", "reactor.core.publisher.Mono<com.niksob.layer_connector.entity.user.UserEntity>", "java.lang.String"),
                        new MethodSignature("getCurrentUser", "reactor.core.publisher.Mono<com.niksob.layer_connector.entity.user.UserEntity>", null),
                        new MethodSignature("clearCache", "void", null),
                        new MethodSignature("get", "java.lang.String", "java.lang.String"),
                        new MethodSignature("createEntityNotFoundException", "RuntimeException", "java.lang.String")
                ));
    }
}
