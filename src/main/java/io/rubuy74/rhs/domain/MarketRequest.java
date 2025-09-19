package io.rubuy74.rhs.domain;

import java.sql.Timestamp;

public class MarketRequest {
    Event event;
    Market market;
    Timestamp timestamp;
    public MarketRequest(Event event, Timestamp timestamp, Market market) {
        this.event = event;
        this.timestamp = timestamp;
        this.market = market;
    }
}
