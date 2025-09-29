package io.rubuy74.rhs.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.rubuy74.rhs.domain.market.MarketOperation;
import io.rubuy74.rhs.port.in.MarketChangeUseCase;
import io.rubuy74.rhs.port.out.MarketChangePublisher;

public record IngestMarketChange(MarketChangePublisher publisher) implements MarketChangeUseCase {

    @Override
    public void handle(MarketOperation marketOperation) throws JsonProcessingException {
        publisher.publish(marketOperation);
    }
}
