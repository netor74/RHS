package io.rubuy74.rhs.port.out;

import io.rubuy74.rhs.domain.MarketOperation;

public interface MarketChangePublisher {
    void publish(MarketOperation marketOperation);
}
