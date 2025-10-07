package io.rubuy74.rhs.converter.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.rubuy74.rhs.domain.MarketOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MarketOperationSerializer {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(MarketOperationSerializer.class);

    public static byte[] serialize(MarketOperation marketOperation) {
        byte[] payload;
        try {
            payload = mapper.writeValueAsBytes(marketOperation);
            return payload;
        } catch (Exception e) {
            LOGGER.error("operation=serialize_market_operation, " +
                    "msg=Failed to serialize MarketOperation to JSON, " +
                    "error={}", e.getMessage(), e);
            return null;
        }
    }
}
