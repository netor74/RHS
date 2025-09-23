package io.rubuy74.rhs.adapter.in.web;

import io.rubuy74.rhs.domain.Event;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {
    // TODO: Service must (somehow) communicate with Market Operator to get the events from the database
    @GetMapping
    public List<Event> getEvents() {
        return List.of();
    }
}
