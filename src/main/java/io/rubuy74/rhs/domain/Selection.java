package io.rubuy74.rhs.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import io.rubuy74.rhs.utils.ValidatorUtils;

import java.util.Objects;

public class Selection {
    @JsonProperty
    private String id;

    @JsonProperty
    private String name;

    @JsonProperty
    private Double odd;

    public Selection(String id, String name, Double odd) {
        ValidatorUtils.checkArgument(id == null,    "Selection id is null","create_selection");
        ValidatorUtils.checkArgument(name == null,  "Selection name is null","create_selection");
        ValidatorUtils.checkArgument(odd == null,   "Selection odd is null","create_selection");
        ValidatorUtils.checkArgument(Objects.equals(id, ""),    "Selection id is empty","create_selection");
        ValidatorUtils.checkArgument(Objects.equals(name, ""),  "Selection name is empty","create_selection");
        this.id = id;
        this.name = name;
        this.odd = odd;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this.getClass())
                .add("id",id)
                .add("name",name)
                .add("odd",odd)
                .toString();
    }
}
