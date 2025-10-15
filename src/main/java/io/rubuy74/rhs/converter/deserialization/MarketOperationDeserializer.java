package io.rubuy74.rhs.converter.deserialization;

import io.rubuy74.rhs.domain.MarketOperation;
import io.rubuy74.rhs.domain.http.MarketRequest;
import io.rubuy74.rhs.domain.http.OperationType;
import io.rubuy74.rhs.utils.ValidatorUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class MarketOperationDeserializer {
    private static final List<String> ATTRIBUTE_LIST = List.of("marketRequest", "operationType");
    private static final List<String> MARKET_REQUEST_ATTRIBUTE_LIST = List.of("event", "selections");

    @SuppressWarnings("unchecked")
    public static MarketOperation deserialize(LinkedHashMap<String, Object> rawPayload) {
        ValidatorUtils.checkArgument(
                rawPayload == null,
                "MarketOperation payload is null",
                "deserialize_market_operation");
        ValidatorUtils.checkAttributeList(
                rawPayload,
                ATTRIBUTE_LIST
        );
        Map<String, Object> marketRequestMap = (Map<String, Object>) rawPayload.get("marketRequest");
        ValidatorUtils.checkAttributeList(
                marketRequestMap,
                MARKET_REQUEST_ATTRIBUTE_LIST
        );


        Map<String, Object> eventMap = (Map<String, Object>) marketRequestMap.get("event");
        List<Map<String, Object>> selectionsMap = (List<Map<String, Object>>) marketRequestMap.get("selections");

        MarketRequest marketRequest = new MarketRequest((String) marketRequestMap.get("marketId"),
                (String) marketRequestMap.get("marketName"),
                EventDTODeserializer.deserialize(eventMap),
                selectionsMap.stream().map(SelectionDeserializer::deserialize).toList()
        );

        return new MarketOperation(marketRequest,
                OperationType.valueOf((String) rawPayload.get("operationType"))
        );
    }
}
