package io.rubuy74.rhs.adapter.in.web;

import io.rubuy74.rhs.domain.Event;
import io.rubuy74.rhs.exception.EventListingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class EventService {
    public static Logger logger = LoggerFactory.getLogger(EventService.class);
    private final RestClient restClient;

    public EventService(
            RestClient.Builder restClient,
            @Value("${mos.service.base-url}")  String baseUrl
    ) {
        this.restClient = restClient
                .baseUrl(baseUrl + "/api/v1")
                .build();
    }

    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public List<Event> getEvents() {
        ParameterizedTypeReference<List<Event>> responseType =
                new ParameterizedTypeReference<>() {};

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
    }
}
