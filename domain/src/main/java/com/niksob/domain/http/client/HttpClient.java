package com.niksob.domain.http.client;

import com.niksob.domain.exception.http.HttpClientException;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class HttpClient {

    private final WebClient.Builder webBuilder;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(HttpClient.class);

    public <T> Mono<T> sendGetRequest(String uri, Class<T> returnClass) {
        final WebClient client = webBuilder.baseUrl(uri).build();

        return client.get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(returnClass)
                .doOnSuccess(o -> log.info("Http client RECEIVE an answer", null, uri))
                .switchIfEmpty(createHttpClientNotFoundError(uri));
    }

    public <T, R> Mono<R> sendPostRequest(String uri, T body, Class<T> bodyClass, Class<R> resultClass) {
        final WebClient client = webBuilder.baseUrl(uri).build();

        return client.post()
                .body(Mono.just(body), bodyClass)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> createHttpClientNotFoundError(body))
                .bodyToMono(resultClass)
                .doOnSuccess(result -> logSuccess(result, resultClass));
//                .onErrorResume(throwable -> createHttpClientNotFoundError(body));
    }

    private <T> Mono<T> createHttpClientNotFoundError(Object state) {
        log.error("Http client did NOT RECEIVE an answer", null, state);
        return Mono.error(new HttpClientException("Resource not found"));
    }

    private <R> void logSuccess(R result, Class<R> resultClass) {
        if (resultClass.equals(Void.class)) {
            return;
        }
        log.info("Http client RECEIVE an answer", null, result);
    }
}
