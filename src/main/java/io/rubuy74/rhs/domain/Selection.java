package io.rubuy74.rhs.domain;

public class Selection {
    String id;
    String name;
    Double odd;

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
