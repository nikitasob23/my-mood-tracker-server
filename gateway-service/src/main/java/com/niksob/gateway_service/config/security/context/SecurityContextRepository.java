package com.niksob.gateway_service.config.security.context;

import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Service
@NoArgsConstructor
public class SecurityContextRepository implements ServerSecurityContextRepository {
    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(SecurityContextRepository.class);

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        log.info("Saving SecurityContext: {}", context);
        exchange.getAttributes().put(SecurityContext.class.getName(), context);
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange.getAttribute(SecurityContext.class.getName()))
                .cast(SecurityContext.class)
                .doOnNext(context -> log.info("Loaded SecurityContext from exchange attributes: {}", context))
                .switchIfEmpty(Mono.defer(() -> {
                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    if (authentication != null) {
                        SecurityContext securityContext = new SecurityContextImpl(authentication);
                        log.info("Created new SecurityContext from SecurityContextHolder: {}", securityContext);
                        return Mono.just(securityContext);
                    }
                    log.info("No SecurityContext found in exchange attributes or SecurityContextHolder");
                    return Mono.empty();
                }));
    }
}
