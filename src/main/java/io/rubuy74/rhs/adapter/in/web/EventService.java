package io.rubuy74.rhs.adapter.in.web;

import io.rubuy74.rhs.config.property.RetryConfig;
import io.rubuy74.rhs.domain.Event;
import io.rubuy74.rhs.exception.EventListingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;

@Service
public class EventService {
    public static Logger LOGGER = LoggerFactory.getLogger(EventService.class);
    private final WebClient webClient;
    private final RetryConfig retryConfig;

    public EventService(
            WebClient.Builder webClientBuilder,
            @Value("${mos.service.base-url}")  String baseUrl,
            RetryConfig retryConfig
    ) {
        final int timeout = retryConfig.getTimeoutMs();
        this.webClient = webClientBuilder
                .baseUrl(baseUrl + "/api/v1")
                .clientConnector(
                        new ReactorClientHttpConnector(
                                HttpClient.create().responseTimeout(Duration.ofMillis(timeout))
                        )
                )
                .build();
        this.retryConfig = retryConfig;
    }

    public Mono<List<Event>> getEvents() {
        ParameterizedTypeReference<List<Event>> responseType =
                new ParameterizedTypeReference<>() {};

        final int maxRetries = retryConfig.getMaxRetries();
        final int backoff = retryConfig.getBackoffTimeMs();

        return webClient.get()
                .uri("/events")
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        (response)-> {
                            LOGGER.error("operation=getEvents, msg='Failed to retrieve events, status={}", response.statusCode());
                            throw new EventListingException(
                                    "Failed to retrieve events. Status" + response.statusCode()
                            );
                        })
                .onStatus(HttpStatusCode::is5xxServerError,
                        (response) -> {
                            LOGGER.error("operation=getEvents, msg=Internal Server Problem, status={}", response.statusCode());
                            throw new EventListingException(
                                    "MOS service error. Status: " + response.statusCode());
                        })
                .bodyToMono(responseType)
                .retryWhen(Retry.backoff(maxRetries,Duration.ofMillis(backoff))
                        .filter(throwable -> throwable instanceof ResourceAccessException | throwable instanceof EventListingException)
                        .doBeforeRetry(retrySignal -> {
                            LOGGER.error("operation=getEvents, msg=Retrying attempt {}, status={}",
                                    retrySignal.totalRetries() + 1,
                                    retrySignal.failure().getMessage());
                        })
                        .onRetryExhaustedThrow(((retryBackoffSpec, retrySignal) -> {
                            LOGGER.error("operation=getEvents, msg=Retries exhausted after {} attempts, lastError={}",
                                    retrySignal.totalRetries() + 1,
                                    retrySignal.failure().getMessage()
                            );
                            throw new EventListingException("Event retrieval failed after all retries. Status= "+ retrySignal.failure().getMessage());
                        }))
                );
    }
}
