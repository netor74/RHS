package io.rubuy74.rhs.adapter.in.web;

import io.rubuy74.rhs.config.property.RetryConfig;
import io.rubuy74.rhs.domain.Event;
import io.rubuy74.rhs.exception.EventListingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class EventService {
    public static Logger logger = LoggerFactory.getLogger(EventService.class);
    private final RestClient restClient;
    private final RetryConfig retryConfig;

    public EventService(
            RestClient.Builder restClient,
            @Value("${mos.service.base-url}")  String baseUrl,
            RetryConfig retryConfig
    ) {
        this.restClient = restClient
                .baseUrl(baseUrl + "/api/v1")
                .build();
        this.retryConfig = retryConfig;
    }

    public List<Event> getEvents() throws InterruptedException {
        ParameterizedTypeReference<List<Event>> responseType =
                new ParameterizedTypeReference<>() {};

        int maxRetries = retryConfig.getMaxRetries();
        int backoff = retryConfig.getBackoff();

        for (int i = 0; i <= maxRetries; i++) {
            try {
                return restClient.get()
                        .uri("/events")
                        .retrieve()
                        .onStatus(HttpStatusCode::is4xxClientError,
                                (request,response)-> {

                                    logger.error("operation=getEvents, msg='Failed to retrieve events, status={}", response.getStatusCode());
                                    throw new EventListingException(
                                            "Failed to retrieve events. Status" + response.getStatusCode()
                                    );
                                })
                        .onStatus(HttpStatusCode::is5xxServerError,
                                (request, response) -> {
                                    logger.error("operation:getEvents, msg:Internal Server Problem, status: {}", response.getStatusCode());
                                    throw new EventListingException(
                                            "MOS service error. Status: " + response.getStatusCode());
                                })
                        .body(responseType);
            } catch (ResourceAccessException | EventListingException e) {
                if (i < maxRetries) {
                    logger.info("operation=getEvents, msg=Could not retrieve events. Retrying in {}ms..., status={}", backoff, e.getMessage());
                    Thread.sleep(backoff);
                    continue;
                } else {
                    logger.error("operation=getEvents, msg=Could not retrieve events after {} retries, status={}",maxRetries,e.getMessage());
                    throw new EventListingException("Event retrieval failed after all tries. Status: " + e.getMessage());
                }
            }
        }
        throw new EventListingException("Retries exhausted");
    }
}
