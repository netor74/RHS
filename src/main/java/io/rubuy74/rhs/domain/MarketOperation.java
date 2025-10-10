package io.rubuy74.rhs.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import io.rubuy74.rhs.domain.http.MarketRequest;
import io.rubuy74.rhs.domain.http.OperationType;
import io.rubuy74.rhs.utils.ValidatorUtils;

public class MarketOperation {
    @JsonProperty
    private MarketRequest marketRequest;
    @JsonProperty
    private OperationType operationType;

    public MarketRequest getMarketRequest() {
        return marketRequest;
    }
    public void setMarketRequest(MarketRequest marketRequest) {
        this.marketRequest = marketRequest;
    }
    public OperationType getOperationType() {
        return operationType;
    }
    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public MarketOperation() {}
    public MarketOperation(MarketRequest marketRequest, OperationType operationType) {
        ValidatorUtils.checkArgument(marketRequest == null,"MarketOperation id is null","create_event");
        ValidatorUtils.checkArgument(operationType == null,"MarketOperation name is null","create_event");
        this.marketRequest = marketRequest;
        this.operationType = operationType;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this.getClass())
                .add("marketRequest", marketRequest)
                .add("operationType", operationType).toString();
    }
}
