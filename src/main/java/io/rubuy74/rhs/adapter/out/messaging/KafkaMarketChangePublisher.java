package io.rubuy74.rhs.adapter.out.messaging;

import io.rubuy74.rhs.converter.serialization.MarketOperationSerializer;
import io.rubuy74.rhs.domain.MarketOperation;
import io.rubuy74.rhs.port.out.MarketChangePublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class KafkaMarketChangePublisher implements MarketChangePublisher {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaMarketChangePublisher.class);
    private static final String TOPIC = "market-changes";
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public KafkaMarketChangePublisher(KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(MarketOperation marketOperation) {
        try {
            MarketOperationSerializer serializer = new MarketOperationSerializer();
            byte[] payload = serializer.serialize(marketOperation);
            kafkaTemplate.send(TOPIC, payload)
                .whenComplete((result, e) -> {
                    if (e != null) {
                        LOGGER.error("operation=publish," +
                                "msg=Failed to send MarketOperation message, " +
                                "error={}", e.getMessage(), e);
                    } else  {
                        LOGGER.info("operation=publish," +
                                "msg=Sent MarketOperation to market-changes, " +
                                "payload={}", marketOperation.toString());
                    }
                }
            );
        } catch (Exception e) {
            LOGGER.error("operation=publish, " +
                    "msg=Caught Exception while sending MarketOperation message, " +
                    "error= {}", e.getMessage(), e);
        }

    }
}
