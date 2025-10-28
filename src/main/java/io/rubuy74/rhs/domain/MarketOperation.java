package io.rubuy74.rhs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import io.rubuy74.rhs.domain.http.MarketRequest;
import io.rubuy74.rhs.domain.http.OperationType;
import io.rubuy74.rhs.utils.ValidatorUtils;

@JsonIgnoreProperties(ignoreUnknown = true)

public class MarketOperation {
    @JsonProperty
    private String requestId;
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
    public String getRequestId() {
        return requestId;
    }
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
    public OperationType getOperationType() {
        return operationType;
    }
    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public MarketOperation() {}
    public MarketOperation(String requestId, MarketRequest marketRequest, OperationType operationType) {
        ValidatorUtils.checkArgument(requestId == null || requestId.isBlank(),"Request ID is null or blank","create_market_operation");
        ValidatorUtils.checkArgument(marketRequest == null,"Market Request is null","create_market_operation");
        ValidatorUtils.checkArgument(operationType == null,"OperationType is null","create_market_operation");
        this.requestId = requestId;
        this.marketRequest = marketRequest;
        this.operationType = operationType;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this.getClass())
                .add("requestId", requestId)
                .add("marketRequest", marketRequest)
                .add("operationType", operationType).toString();
    }
}
