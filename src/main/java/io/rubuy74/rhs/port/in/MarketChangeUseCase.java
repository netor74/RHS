package io.rubuy74.rhs.port.in;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.rubuy74.rhs.domain.MarketOperation;

public interface MarketChangeUseCase {
    void handle(MarketOperation marketOperation) throws JsonProcessingException;
}
