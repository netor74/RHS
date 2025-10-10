package io.rubuy74.rhs.adapter.in.web;

import io.rubuy74.rhs.domain.Event;
import io.rubuy74.rhs.exception.EventListingException;
import org.springframework.beans.factory.annotation.Value;
import io.rubuy74.rhs.domain.http.EventListingResponse;
import io.rubuy74.rhs.domain.http.Status;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {
    private final RestClient restClient;
    private final EventService eventService;

    public EventController(
            RestClient.Builder restClient,
            @Value("${mos.service.base-url}")  String baseUrl,
            EventService eventService
    ) {
        this.eventService = eventService;
        this.restClient = restClient
                .baseUrl(baseUrl + "/api/v1")
                .build();
    }

    @GetMapping
    public EventListingResponse getEvents() {
        try {
            List<Event> events = eventService.getEvents(restClient);
            return new EventListingResponse(Status.SUCCESS,"",events);

        } catch (EventListingException  e) {
            EventListingResponse eventListingResponse = new EventListingResponse(
                    Status.ERROR,
                    e.getMessage() != null ? e.getMessage() : "Unknown error.",
                    List.of()
            );

            return ResponseEntity
                    .status(HttpStatus.SERVICE_UNAVAILABLE) // Use 503 for external dependency failure
                    .body(eventListingResponse).getBody();
        } catch (Exception e) {
            EventListingResponse eventListingResponse = new EventListingResponse(
                    Status.ERROR,
                    e.getMessage() != null ? e.getMessage() : "Unknown error.",
                    List.of()
            );
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR) // Use 500 for internal failure
                    .body(eventListingResponse).getBody();
        }
    }
}
