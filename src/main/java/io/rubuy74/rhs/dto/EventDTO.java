package io.rubuy74.rhs.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import io.rubuy74.rhs.converter.deserialization.LocalDateToEpochDeserializer;
import io.rubuy74.rhs.domain.Event;

import java.time.ZoneId;
import java.time.ZoneOffset;


public class EventDTO {

    private static final ZoneId zoneId = ZoneOffset.UTC;

    @JsonProperty
    private String id;

    @JsonProperty
    private String name;

    @JsonProperty("date")
    private long epochMilliseconds;

    public EventDTO() {}

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public long getEpochMilliseconds() {
        return epochMilliseconds;
    }

    public EventDTO(Event event) {
        this.id = event.getId();
        this.name = event.getName();
        this.epochMilliseconds = event.getDate();
    }

    public EventDTO(String id, String name, long epochMilliseconds) {
        this.id = id;
        this.name = name;
        this.epochMilliseconds= epochMilliseconds;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this.getClass())
                .add("id",id)
                .add("name", name)
                .add("date",epochMilliseconds)
                .toString();
    }

}
