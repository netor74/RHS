package io.rubuy74.rhs.domain.http;

import io.rubuy74.rhs.domain.MarketOperation;

public record MarketOperationResult(ResultType resultType, String message, MarketOperation marketOperation) { }
