package io.rubuy74.rhs.port.in;

import io.rubuy74.rhs.domain.MarketOperation;
import io.rubuy74.rhs.domain.MarketRequest;

public interface MarketChangeUseCase {
    void handle(MarketOperation marketOperation);
}
