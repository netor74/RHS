package io.rubuy74.rhs.converter;

import io.rubuy74.rhs.domain.MarketOperation;
import io.rubuy74.rhs.domain.Selection;
import io.rubuy74.rhs.domain.http.MarketRequest;
import io.rubuy74.rhs.domain.http.OperationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MarketOperationJSONConverter {
    private static final Logger logger = LoggerFactory.getLogger(MarketOperationJSONConverter.class);

    @SuppressWarnings("unchecked")
    public static MarketOperation fromJson(LinkedHashMap<String, Object> rawPayload) {
        MarketOperation marketOperation = new MarketOperation();
        MarketRequest marketRequest = new MarketRequest();

        if(!rawPayload.containsKey("marketRequest")) {
            logger.error("operation= deserialize_market_operation, " +
                    "msg=MarketRequest doesn't exist in payload");
            return null;
        }
        Map<String, Object> marketRequestMap = (Map<String, Object>) rawPayload.get("marketRequest");
        if(!rawPayload.containsKey("event")) {
            logger.error("operation= deserialize_market_operation, " +
                    "msg=Event doesn't exist in payload");
            return null;
        }
        Map<String, Object> eventMap = (Map<String, Object>) marketRequestMap.get("event");

        if(!rawPayload.containsKey("selection")) {
            logger.error("operation= deserialize_market_operation, " +
                    "msg=Selection doesn't exist in payload");
            return null;
        }

        // add event to marketRequest
        marketRequest.eventDTO = EventDTOJSONConverter.fromJson(eventMap);
        marketRequest.marketId = (String)marketRequestMap.get("marketId");
        marketRequest.marketName = (String)marketRequestMap.get("marketName");

        List<Map<String, Object>> selectionsMap = (List<Map<String, Object>>) marketRequestMap.get("selections");

        // add selections to marketRequest
        marketRequest.selections = selectionsMap.stream().map(Selection::fromJson).toList();
        marketOperation.setMarketRequest(marketRequest);
        marketOperation.setOperationType(OperationType.valueOf((String) marketRequestMap.get("operationType")));
        return marketOperation;
    }
}
