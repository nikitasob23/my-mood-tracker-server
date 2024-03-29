package com.niksob.domain.http.client;

import com.niksob.domain.exception.rest.controller.response.HttpClientException;
import com.niksob.domain.model.rest.response.error.ErrorDetails;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
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
                .onStatus(HttpStatusCode::isError, response -> createHttpClientNotFoundError(response, uri))
                .bodyToMono(returnClass)
                .doOnSuccess(o -> log.info("Http client RECEIVE an answer", null, uri));
    }

    public <T, R> Mono<R> sendPostRequest(String uri, T body, Class<T> bodyClass, Class<R> resultClass) {
        final WebClient client = webBuilder.baseUrl(uri).build();
        return sendRequest(client.post(), body, bodyClass, resultClass);
    }

    public <T, R> Mono<R> sendPutRequest(String uri, T body, Class<T> bodyClass, Class<R> resultClass) {
        final WebClient client = webBuilder.baseUrl(uri).build();
        return sendRequest(client.put(), body, bodyClass, resultClass);
    }

    private <T, R> Mono<R> sendRequest(
            WebClient.RequestBodyUriSpec client, T body, Class<T> bodyClass, Class<R> resultClass
    ) {
        return client.body(Mono.just(body), bodyClass)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> createHttpClientNotFoundError(response, body))
                .bodyToMono(resultClass)
                .doOnSuccess(result -> logSuccess(result, resultClass));
    }

    private <T> Mono<T> createHttpClientNotFoundError(ClientResponse response, Object logState) {
        return response.bodyToMono(ErrorDetails.class)
                .doOnNext(answer -> log.error("Http client did NOT RECEIVE an answer", null, logState))
                .flatMap(errorDetails -> Mono.error(new HttpClientException(
                        HttpStatus.valueOf(errorDetails.getStatus()),
                        errorDetails.getMessage(),
                        errorDetails.getTimestamp(),
                        errorDetails.getPath()
                )));
    }

    private <R> void logSuccess(R result, Class<R> resultClass) {
        if (resultClass.equals(Void.class)) {
            return;
        }
        log.info("Http client RECEIVE an answer", null, result);
    }
}
