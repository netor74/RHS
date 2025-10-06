package io.rubuy74.rhs.adapter.in.web;

import io.rubuy74.rhs.domain.Event;
import io.rubuy74.rhs.exception.EventListingException;
import io.rubuy74.rhs.domain.http.EventListingResponse;
import io.rubuy74.rhs.domain.http.Status;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public Mono<ResponseEntity<EventListingResponse>> getEvents() throws InterruptedException {
        return eventService.getEvents()
                .map(events -> ResponseEntity.ok(new EventListingResponse(Status.SUCCESS,"",events)))
                // Handle specific domain exception first
                .onErrorResume(EventListingException.class, e ->
                        Mono.just(ResponseEntity
                                .status(HttpStatus.SERVICE_UNAVAILABLE)
                                .body(
                                        new EventListingResponse(
                                                Status.ERROR,
                                                e.getMessage(),
                                                List.of()
                                        )
                                )
                        )
                )
                // Fallback for any other runtime exceptions
                .onErrorResume(RuntimeException.class, e ->
                        Mono.just(ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(
                                        new EventListingResponse(
                                                Status.ERROR,
                                                "Unknown Internal Server Error",
                                                List.of()
                                        )
                                )
                        )
                )
                .onErrorReturn(ResponseEntity.notFound().build());
    }
}
