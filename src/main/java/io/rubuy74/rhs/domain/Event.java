package io.rubuy74.rhs.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import io.rubuy74.rhs.utils.ValidatorUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Event {
    @JsonProperty
    private final String id;

    @JsonProperty
    private final String name;

    @JsonProperty
    @JsonFormat(pattern = "dd/MM/yyyy")
    private final LocalDate date;

    @JsonProperty
    private final List<Market> markets = new java.util.ArrayList<>();

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public LocalDate getDate() {
        return date;
    }

    public Event(String id, String name, LocalDate date) {
        ValidatorUtils.checkArgument(id == null,"Event id is null","create_event");
        ValidatorUtils.checkArgument(name == null,"Event name is null","create_event");
        ValidatorUtils.checkArgument(date == null,"Event date is null","create_event");
        ValidatorUtils.checkArgument(id.isBlank(),   "Event id is empty","create_event");
        ValidatorUtils.checkArgument(name.isBlank(), "Event name is empty","create_event");
        this.id = id;
        this.name = name;
        this.date = date;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this.getClass())
                .add("id",id)
                .add("name",name)
                .add("date",date)
                .add("market",markets)
                .toString();
    }
}
