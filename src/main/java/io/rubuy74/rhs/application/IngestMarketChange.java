package io.rubuy74.rhs.application;

import io.rubuy74.rhs.domain.MarketOperation;
import io.rubuy74.rhs.port.in.MarketChangeUseCase;
import io.rubuy74.rhs.port.out.MarketChangePublisher;

public class IngestMarketChange implements MarketChangeUseCase {
    private final MarketChangePublisher publisher;
    public IngestMarketChange(MarketChangePublisher publisher) {
        this.publisher = publisher;
    }

    // TODO: transform MarketRequest into MarketOperation && apply business rules
    @Override
    public void handle(MarketOperation marketOperation) {
        // TODO: see if event is not in the future
        // TODO: see if the event & market already exists (for add)
        // TODO: see if event & market doesn't exist (for edit, delete)

        publisher.publish(marketOperation);
    }

}
