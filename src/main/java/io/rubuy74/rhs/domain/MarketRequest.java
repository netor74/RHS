package io.rubuy74.rhs.domain;

import java.sql.Timestamp;
import java.util.List;

public class MarketRequest {
    String marketId;
    String marketName;
    Event event;
    Timestamp timestamp;
    List<Selection> selections;

    public MarketRequest(String marketId, String marketName,Event event, List<Selection> selections) {
        this.marketId = marketId;
        this.marketName = marketName;
        this.event = event;
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.selections = selections;
    }
    @Override
    public String toString() {
        return "MarketRequest{" +
                "marketId='" + marketId + '\'' +
                ", marketName='" + marketName + '\'' +
                ", event=" + event +
                ", selections=" + selections +
                "}";
    }
}
