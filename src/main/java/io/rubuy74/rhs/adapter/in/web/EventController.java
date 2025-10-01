package io.rubuy74.rhs.adapter.in.web;

import io.rubuy74.rhs.domain.Event;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import io.rubuy74.rhs.domain.http.EventListingResponse;
import io.rubuy74.rhs.domain.http.Status;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {
    private final RestClient restClient;

    public EventController(
            RestClient.Builder restClient,
            @Value("${mos.service.base-url}")  String baseUrl
    ) {

        this.restClient = restClient
                .baseUrl(baseUrl)
                .build();
    }

    @GetMapping
    public EventListingResponse getEvents() {
        ParameterizedTypeReference<List<Event>> responseType =
                new ParameterizedTypeReference<>() {};
        try {
            List<Event> events = restClient.get()
                    .uri("/api/v1/events")
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError,
                            (request,response)-> {
                                throw new RuntimeException(
                                        "Failed to retrieve events. Status" + response.getStatusCode()
                                );
                    })
                    .onStatus(HttpStatusCode::is5xxServerError,
                            (request, response) -> {
                                throw new RuntimeException(
                                        "MOS service error. Status: " + response.getStatusCode());
                            })
                    .body(responseType);

            return new EventListingResponse(Status.SUCCESS,"",events);

        } catch (Exception e) {

            EventListingResponse eventListingResponse = new EventListingResponse(
                    Status.ERROR,
                    e.getMessage() != null ? e.getMessage() : "Unknown error.",
                    List.of()
            );

            return ResponseEntity
                    .status(HttpStatus.SERVICE_UNAVAILABLE) // Use 503 for external dependency failure
                    .body(eventListingResponse).getBody();
        }
    }
}
