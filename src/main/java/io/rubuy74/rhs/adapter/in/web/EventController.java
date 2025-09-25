package io.rubuy74.rhs.adapter.in.web;

import io.rubuy74.rhs.domain.Event;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {
    private final String baseUrl;
    private final RestClient restClient;

    public EventController(
            RestClient.Builder restClient,
            @Value("${mos.service.base-url}")  String baseUrl
    ) {

        this.restClient = restClient
                .baseUrl(baseUrl)
                .build();
        this.baseUrl = baseUrl;
    }

    @GetMapping
    public List<Event> getEvents() {

        ParameterizedTypeReference<List<Event>> responseType =
                new ParameterizedTypeReference<>() {};
        try {
            return restClient.get()
                    .uri("/api/v1/events")
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                            (request, response) -> {
                                throw new RuntimeException("MOS service error. Status: " + response.getStatusCode());
                            })
                    .body(responseType);

        } catch (Exception e) {
            throw new RuntimeException("Error fetching events from MOS service.", e);
        }
    }
}
