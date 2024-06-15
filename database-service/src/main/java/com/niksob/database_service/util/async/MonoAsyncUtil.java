package com.niksob.database_service.util.async;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.Callable;

public class MonoAsyncUtil {
    public static <T> Mono<T> create(Callable<T> callable) {
        return Mono.defer(() -> Mono.fromCallable(callable))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public static Mono<Void> create(Runnable runnable) {
        return Mono.defer(() -> Mono.fromRunnable(runnable))
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }
}
