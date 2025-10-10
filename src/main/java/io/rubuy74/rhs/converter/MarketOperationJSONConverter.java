package io.rubuy74.rhs.converter;

import io.rubuy74.rhs.domain.MarketOperation;
import io.rubuy74.rhs.domain.Selection;
import io.rubuy74.rhs.domain.http.MarketRequest;
import io.rubuy74.rhs.domain.http.OperationType;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MarketOperationJSONConverter {
    @SuppressWarnings("unchecked")
    public static MarketOperation fromJson(LinkedHashMap<String, Object> rawPayload) {
        MarketOperation marketOperation = new MarketOperation();
        MarketRequest marketRequest = new MarketRequest();

        Map<String, Object> marketRequestMap = (Map<String, Object>) rawPayload.get("marketRequest");
        Map<String, Object> eventMap = (Map<String, Object>) marketRequestMap.get("event");

        // add event to marketRequest
        marketRequest.eventDTO = EventDTOJSONConverter.fromJson(eventMap);
        marketRequest.marketId = (String)marketRequestMap.get("marketId");
        marketRequest.marketName = (String)marketRequestMap.get("marketName");

        if(marketRequestMap.containsKey("selections")){
            List<Map<String, Object>> selectionsMap = (List<Map<String, Object>>) marketRequestMap.get("selections");

            // add selections to marketRequest
            marketRequest.selections = selectionsMap.stream().map(Selection::fromJson).toList();
        }

        marketOperation.setMarketRequest(marketRequest);
        marketOperation.setOperationType(OperationType.valueOf((String) marketRequestMap.get("operationType")));
        return marketOperation;
    }
}
