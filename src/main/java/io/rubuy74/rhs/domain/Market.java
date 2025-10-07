package io.rubuy74.rhs.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.rubuy74.rhs.utils.ValidatorUtils;

import java.util.List;

public class Market {
    @JsonProperty
    private String id;

    @JsonProperty
    private String name;

    @JsonProperty
    private List<Selection> selections;

    public Market(String id, String name, List<Selection> selections) {
        ValidatorUtils.checkArgument(id == null,        "Market id is null","create_event");
        ValidatorUtils.checkArgument(name == null,      "Market name is null","create_event");
        ValidatorUtils.checkArgument(selections == null,"Market date is null","create_event");
        this.id = id;
        this.name = name;
        this.selections = new java.util.ArrayList<>(selections);
    }
}
