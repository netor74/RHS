package io.rubuy74.rhs.adapter.in.messaging;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.rubuy74.rhs.domain.MarketOperationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class KafkaMarketStatusConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaMarketStatusConsumer.class);
    private static final String topic = "market-change-status";
    private static final String groupId = "market-status-group";

    private final ConcurrentHashMap<String, MarketOperationResult> cache;
    private final ObjectMapper mapper;

    public KafkaMarketStatusConsumer(ConcurrentHashMap<String, MarketOperationResult> cache) {
        this.cache = cache;
        this.mapper = new ObjectMapper();
        this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @KafkaListener(topics = topic, groupId = groupId, containerFactory = "byteArrayKafkaListenerContainerFactory")
    private void handleMessage(byte[] rawPayload) {
        try {
            MarketOperationResult result = mapper.readValue(rawPayload, MarketOperationResult.class);
            if (result.requestId() != null) {
                cache.put(result.requestId(), result);
                logger.info("operation=consume_market_status, msg=Cached result for requestId={}, status={}", result.requestId(), result.resultType());
            }
        } catch (Exception e) {
            logger.error("operation=consume_market_status, msg=Failed to deserialize MarketOperationResult, error={}", e.getMessage(), e);
        }
    }
}