package io.rubuy74.rhs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MarketOperationResult(String requestId, String resultType, String message, MarketOperation marketOperation) { }