package io.rubuy74.rhs.adapter.out.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.rubuy74.rhs.domain.MarketOperation;
import io.rubuy74.rhs.port.out.MarketChangePublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaMarketChangePublisher implements MarketChangePublisher {
    private static final Logger logger = LoggerFactory.getLogger(KafkaMarketChangePublisher.class);
    private static final String TOPIC = "market-changes";
    private static final ObjectMapper mapper = new ObjectMapper() ;
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public KafkaMarketChangePublisher(KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(MarketOperation marketOperation) {

        byte[] payload;
        try {
            payload = mapper.writeValueAsBytes(marketOperation);

        } catch (Exception e) {
            logger.error("operation=serialize_market_operation, " +
                    "msg=Failed to serialize MarketOperation to JSON, " +
                    "error={}", e.getMessage(), e);
            return;
        }

        try {
            CompletableFuture<SendResult<String,byte[]>> future= kafkaTemplate.send(TOPIC, payload);
            future.whenComplete((result, e) -> {
                if (e != null) {
                    logger.error("operation=send_market_operation," +
                            "msg=Failed to send MarketOperation message, " +
                            "error:{}", e.getMessage(), e);
                } else  {
                    logger.info("operation=send_market_operation," +
                            "msg=Sent MarketOperation to market-changes: " +
                            "payload={}", payload);
                }
            });
        } catch (Exception e) {
            logger.error("operation=send_market_operation, " +
                    "msg:Caught Exception while sending MarketOperation message, " +
                    "error: {}", e.getMessage(), e);
        }

    }
}
