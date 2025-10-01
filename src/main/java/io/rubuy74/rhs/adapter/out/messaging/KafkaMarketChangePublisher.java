package io.rubuy74.rhs.adapter.out.messaging;

import org.apache.kafka.common.serialization.ByteArraySerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.rubuy74.rhs.domain.MarketOperation;
import io.rubuy74.rhs.port.out.MarketChangePublisher;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Properties;
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
            logger.error("Failed to serialize MarketOperation to JSON: {}", e.getMessage(), e);
            return;
        }

        CompletableFuture<SendResult<String,byte[]>> future= kafkaTemplate.send(TOPIC, payload);
        future.whenComplete((result, e) -> {
            if (e != null) {
                logger.error("Failed to send MarketOperation message: {}", e.getMessage(), e);
            } else  {
                logger.info("Sent MarketOperation to market-changes: {}", payload);
            }
        });
    }
}
