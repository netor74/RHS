package io.rubuy74.rhs.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Event {
    @JsonProperty
    String id;

    @JsonProperty
    String name;

    @JsonProperty
    String date;

    @JsonProperty
    private List<Market> markets = new java.util.ArrayList<>();

    public Event(String id, String name, String date) {
        this.id = id;
        this.name = name;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Event{"
                + "id='" + id + '\''
                + ", name='" + name + '\''
                + ", date=" + date
                + ", markets=" + markets;
    }
}
