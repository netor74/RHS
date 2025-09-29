package io.rubuy74.rhs.port.out;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.rubuy74.rhs.domain.market.MarketOperation;

public interface MarketChangePublisher {
    void publish(MarketOperation marketOperation) throws JsonProcessingException;
}
