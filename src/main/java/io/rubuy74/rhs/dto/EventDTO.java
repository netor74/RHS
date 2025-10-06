package io.rubuy74.rhs.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import io.rubuy74.rhs.domain.Event;

import java.time.LocalDate;

public class EventDTO {
    @JsonProperty
    private String id;

    @JsonProperty
    private String name;

    @JsonProperty
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;

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
    
    public LocalDate getDate() {
        return date;
    }
    
    public EventDTO(Event event) {
        this.id = event.getId();
        this.name = event.getName();
        this.date = event.getDate();
    }

    public EventDTO(String id, String name, LocalDate date) {
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
