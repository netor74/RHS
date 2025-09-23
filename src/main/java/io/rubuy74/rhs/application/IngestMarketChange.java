package io.rubuy74.rhs.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.rubuy74.rhs.domain.MarketOperation;
import io.rubuy74.rhs.port.in.MarketChangeUseCase;
import io.rubuy74.rhs.port.out.MarketChangePublisher;

public record IngestMarketChange(MarketChangePublisher publisher) implements MarketChangeUseCase {

    // TODO: transform MarketRequest into MarketOperation && apply business rules
    @Override
    public void handle(MarketOperation marketOperation) throws JsonProcessingException {
        // TODO: see if event is not in the future
        // TODO: see if the event & market already exists (for add)
        // TODO: see if event & market doesn't exist (for edit, delete)

        publisher.publish(marketOperation);
    }
}
