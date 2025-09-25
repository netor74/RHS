package io.rubuy74.rhs.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.util.List;

public class MarketRequest {
    @JsonProperty
    String marketId;

    @JsonProperty
    String marketName;

    @JsonProperty("event")
    EventDTO eventDTO;

    @JsonProperty
    Timestamp timestamp;

    @JsonProperty
    List<Selection> selections;

    public MarketRequest(String marketId, String marketName,EventDTO eventDTO, List<Selection> selections) {
        this.marketId = marketId;
        this.marketName = marketName;
        this.eventDTO = eventDTO;
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.selections = selections;
    }
    @Override
    public String toString() {
        return "MarketRequest{" +
                "marketId='" + marketId + '\'' +
                ", marketName='" + marketName + '\'' +
                ", event=" + eventDTO +
                ", selections=" + selections +
                "}";
    }
}
