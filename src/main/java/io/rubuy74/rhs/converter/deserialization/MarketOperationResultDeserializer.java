package io.rubuy74.rhs.converter.deserialization;

import io.rubuy74.rhs.domain.MarketOperation;
import io.rubuy74.rhs.domain.http.MarketOperationResult;
import io.rubuy74.rhs.domain.http.ResultType;
import io.rubuy74.rhs.utils.ValidatorUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MarketOperationResultDeserializer {
    private static final List<String> ATTRIBUTE_LIST = List.of("resultType","message","marketOperation");

    @SuppressWarnings("unchecked")
    public static MarketOperationResult deserialize(Map<String, Object> rawPayload) {
        ValidatorUtils.checkArgument(
                rawPayload == null,
                "MarketOperation payload is null",
                "deserialize_market_operation_result"
        );

        ValidatorUtils.checkAttributeList(
                rawPayload,
                ATTRIBUTE_LIST
        );

        LinkedHashMap<String, Object> marketOperationMap = (LinkedHashMap<String, Object>) rawPayload.get("marketOperation");
        MarketOperation marketOperation = MarketOperationDeserializer.deserialize(marketOperationMap);
        System.out.println("sanity check 4");

        return new MarketOperationResult(
                ResultType.valueOf((String) rawPayload.get("resultType")),
                (String) rawPayload.get("message"),
                marketOperation
        );
    }
}
