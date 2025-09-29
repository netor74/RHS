package io.rubuy74.rhs.domain.http;

import io.rubuy74.rhs.domain.event.Event;

import java.util.List;

public record EventListingResponse(Status status, String message, List<Event> events) { }
