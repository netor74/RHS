package io.rubuy74.rhs.application;

import io.rubuy74.rhs.domain.MarketRequest;
import io.rubuy74.rhs.port.in.MarketChangeUseCase;
import io.rubuy74.rhs.port.out.MarketChangePublisher;

public class IngestMarketChange implements MarketChangeUseCase {
    private MarketChangePublisher publisher;
    public IngestMarketChange(MarketChangePublisher publisher) {}

    // TODO: transform MarketRequest into MarketOperation && apply business rules
    @Override
    public void handle(MarketRequest marketRequest) {
        // publisher.publish(marketOperation);
    }
}
