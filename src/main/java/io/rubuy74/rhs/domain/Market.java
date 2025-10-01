package io.rubuy74.rhs.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Market {
    @JsonProperty
    String id;

    @JsonProperty
    String name;

    @JsonProperty
    List<Selection> selections;

    public Market(String id, String name, List<Selection> selections) {
        this.id = id;
        this.name = name;
        this.selections = new java.util.ArrayList<>(selections);
    }
}
