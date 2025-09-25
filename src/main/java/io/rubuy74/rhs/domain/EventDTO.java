package io.rubuy74.rhs.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EventDTO {
    @JsonProperty
    String id;

    @JsonProperty
    String name;

    @JsonProperty
    String date;

    public EventDTO() {}

    public EventDTO(Event event) {
        this.id = event.id;
        this.name = event.name;
        this.date = event.date;
    }

    public EventDTO(String id, String name, String date) {
        this.id = id;
        this.name = name;
        this.date = date;
    }

    @Override
    public String toString() {
        return "EventDTO{"
                + "id='" + id + '\''
                + ", name='" + name + '\''
                + ", date=" + date + '}';
    }
}
