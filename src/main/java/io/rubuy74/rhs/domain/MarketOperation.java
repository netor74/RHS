package io.rubuy74.rhs.domain;

public class MarketOperation {
    public MarketRequest marketRequest;
    public OperationType operationType;

    public MarketOperation(MarketRequest marketRequest, OperationType operationType) {
        this.marketRequest = marketRequest;
        this.operationType = operationType;
    }
}
