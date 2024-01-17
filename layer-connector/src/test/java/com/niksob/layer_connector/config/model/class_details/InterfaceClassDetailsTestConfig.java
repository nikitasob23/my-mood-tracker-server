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
public class InterfaceClassDetailsTestConfig {
    public ClassDetails getDetails() {
        return new ClassDetails(
                "com.niksob.layer_connector.dao.user.UserDao",
                Set.of(
                        new MethodSignature("load", "com.niksob.domain.model.user.UserInfo", "com.niksob.domain.model.user.Username"),
                        new MethodSignature("save", "void", "com.niksob.domain.model.user.UserInfo"),
                        new MethodSignature("update", "com.niksob.domain.model.user.UserInfo", "com.niksob.domain.model.user.UserInfo"),
                        new MethodSignature("delete", "com.niksob.domain.model.user.UserInfo", "com.niksob.domain.model.user.Username"),
                        new MethodSignature("get", "java.lang.String", "java.lang.String"),
                        new MethodSignature("getCurrentUser", "com.niksob.domain.model.user.UserInfo", null)
                ));
    }

    public ClassDetails getDetailsWithGeneric() {
        return new ClassDetails(
                "com.niksob.layer_connector.dao.user.UserDao",
                Set.of(
                        new MethodSignature("load", "reactor.core.publisher.Mono<com.niksob.domain.model.user.UserInfo>", "com.niksob.domain.model.user.Username"),
                        new MethodSignature("save", "void", "com.niksob.domain.model.user.UserInfo"),
                        new MethodSignature("update", "reactor.core.publisher.Mono<com.niksob.domain.model.user.UserInfo>", "com.niksob.domain.model.user.UserInfo"),
                        new MethodSignature("delete", "reactor.core.publisher.Mono<com.niksob.domain.model.user.UserInfo>", "com.niksob.domain.model.user.Username"),
                        new MethodSignature("get", "java.lang.String", "java.lang.String"),
                        new MethodSignature("getCurrentUser", "reactor.core.publisher.Mono<com.niksob.domain.model.user.UserInfo>", null)
                ));
    }
}
