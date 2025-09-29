package io.rubuy74.rhs.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class Selection {
    @JsonProperty
    String id;

    @JsonProperty
    String name;

    @JsonProperty
    Double odd;

    public static Selection fromJson(Map<String,Object> rawPayload) {
        String id = (String) rawPayload.get("id");
        String name = (String) rawPayload.get("name");
        Double odd = (Double) rawPayload.get("odd");
        return new Selection(id, name, odd);
    }

    public Selection(String id, String name, Double odd) {
        this.id = id;
        this.name = name;
        this.odd = odd;
    }

    @Override
    public String toString() {
        return "Selection{" +
                "id:" + id
                + ", name:" + name
                + ", odd:" + odd
                ;
    }
}
