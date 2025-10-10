package io.rubuy74.rhs.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Market {
    @JsonProperty
    private String id;

    @JsonProperty
    private String name;

    @JsonProperty
    private List<Selection> selections;

    public Market(String id, String name, List<Selection> selections) {
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }
        if (name == null) {
            throw new IllegalArgumentException("name is null");
        }
        if (selections == null) {
            throw new IllegalArgumentException("selections is null");
        }
        this.id = id;
        this.name = name;
        this.selections = new java.util.ArrayList<>(selections);
    }
}
