package io.rubuy74.rhs.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class EventDTO {
    @JsonProperty
    private String id;

    @JsonProperty
    private String name;

    @JsonProperty
    private String date;

    public EventDTO() {}

    public static EventDTO fromJson(Map<String,Object> rawPayload) {
        String id = (String) rawPayload.get("id");
        String name = (String) rawPayload.get("name");
        String date = (String) rawPayload.get("date");

        return new EventDTO(id,name,date);
    }

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
        return "EventDTO{"
                + "id='" + id + '\''
                + ", name='" + name + '\''
                + ", date=" + date + '}';
    }
}
