package com.niksob.layer_connector.config.model.class_details;

import com.niksob.layer_connector.model.class_details.ClassDetails;
import com.niksob.layer_connector.model.method_details.MethodSignature;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@NoArgsConstructor
@Getter
public class MapperClassDetailsTestConfig {
    public List<ClassDetails> getDetails() {
        return List.of(
                new ClassDetails(
                        "com.niksob.layer_connector.mapper.user.UserEntityMapper",
                        Set.of(
                                new MethodSignature("toEntityUsername", "java.lang.String", "com.niksob.domain.model.user.Username"),
                                new MethodSignature("toEntityUsername", "com.niksob.domain.model.user.Username", "java.lang.String"),
                                new MethodSignature("toEntity", "com.niksob.layer_connector.entity.user.UserEntity", "com.niksob.domain.model.user.UserInfo")
                        )),
                new ClassDetails(
                        "com.niksob.layer_connector.mapper.user.UserEntityMapper2",
                        Set.of(new MethodSignature("fromEntity", "com.niksob.domain.model.user.UserInfo", "com.niksob.layer_connector.entity.user.UserEntity"))
                ));
    }

    public List<ClassDetails> getIncompleteDetails() {
        return List.of(new ClassDetails(
                "com.niksob.layer_connector.mapper.user.UserEntityMapper",
                Set.of(new MethodSignature("toEntityUsername", "java.lang.String", "com.niksob.domain.model.user.Username"))
        ));
    }

    public List<ClassDetails> getDetailsWithIdenticalReturnTypes() {
        return List.of(new ClassDetails(
                "com.niksob.layer_connector.mapper.user.UserEntityMapper",
                Set.of(
                        new MethodSignature("toEntityUsername", "java.lang.String", "com.niksob.domain.model.user.Username"),
                        new MethodSignature("toEntityUsername2", "java.lang.String", "com.niksob.domain.model.user.Username"),
                        new MethodSignature("toEntityUsername", "com.niksob.domain.model.user.Username", "java.lang.String"),
                        new MethodSignature("toEntity", "com.niksob.layer_connector.entity.user.UserEntity", "com.niksob.domain.model.user.UserInfo"),
                        new MethodSignature("fromEntity", "com.niksob.domain.model.user.UserInfo", "com.niksob.layer_connector.entity.user.UserEntity")
                )));
    }

    public List<ClassDetails> getDetailsWithGeneric() {
        return List.of(new ClassDetails(
                        "com.niksob.layer_connector.mapper.user.UserEntityMapper",
                        Set.of(
                                new MethodSignature("toEntityUsername", "java.lang.String", "com.niksob.domain.model.user.Username"),
                                new MethodSignature("toEntityUsername", "com.niksob.domain.model.user.Username", "java.lang.String"),
                                new MethodSignature("toMonoEntity", "reactor.core.publisher.Mono<com.niksob.layer_connector.entity.user.UserEntity>", "reactor.core.publisher.Mono<com.niksob.domain.model.user.UserInfo>"),
                                new MethodSignature("fromMonoEntity", "reactor.core.publisher.Mono<com.niksob.domain.model.user.UserInfo>", "reactor.core.publisher.Mono<com.niksob.layer_connector.entity.user.UserEntity>")
                        )),
                new ClassDetails(
                        "com.niksob.layer_connector.mapper.user.UserEntityMapper2",
                        Set.of(
                                new MethodSignature("toEntity", "com.niksob.layer_connector.entity.user.UserEntity", "com.niksob.domain.model.user.UserInfo"),
                                new MethodSignature("fromEntity", "com.niksob.domain.model.user.UserInfo", "com.niksob.layer_connector.entity.user.UserEntity"
                                ))
                )
        );
    }
}
