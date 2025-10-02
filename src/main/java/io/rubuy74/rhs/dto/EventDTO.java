package io.rubuy74.rhs.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import io.rubuy74.rhs.domain.Event;

public class EventDTO {
    @JsonProperty
    private String id;

    @JsonProperty
    private String name;

    @JsonProperty
    private String date;

    public EventDTO() {}

    public EventDTO(Event event) {
        this.id = event.getId();
        this.name = event.getName();
        this.date = event.getDate();
    }

    public EventDTO(String id, String name, String date) {
        this.id = id;
        this.name = name;
        this.date = date;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this.getClass())
                .add("id",id)
                .add("name", name)
                .add("date",date)
                .toString();
    }
}
