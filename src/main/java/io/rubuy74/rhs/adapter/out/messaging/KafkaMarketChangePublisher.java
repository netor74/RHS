package io.rubuy74.rhs.adapter.out.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.rubuy74.rhs.converter.deserialization.MarketOperationDeserializer;
import io.rubuy74.rhs.converter.deserialization.MarketOperationResultDeserializer;
import io.rubuy74.rhs.converter.serialization.MarketOperationSerializer;
import io.rubuy74.rhs.domain.MarketOperation;
import io.rubuy74.rhs.domain.http.MarketOperationResult;
import io.rubuy74.rhs.port.out.MarketChangePublisher;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;


@Service
public class KafkaMarketChangePublisher implements MarketChangePublisher {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaMarketChangePublisher.class);
    private static final String TOPIC = "market-changes";
    private final ReplyingKafkaTemplate<String, byte[],LinkedHashMap<String,Object>> kafkaTemplate;

    public KafkaMarketChangePublisher(ReplyingKafkaTemplate<String, byte[], LinkedHashMap<String,Object>> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(MarketOperation marketOperation) {
        try {
            MarketOperationSerializer serializer = new MarketOperationSerializer();
            byte[] payload = serializer.serialize(marketOperation);
            ProducerRecord<String, byte[]> record = new ProducerRecord<>(TOPIC, payload);
            kafkaTemplate.sendAndReceive(record)
                .whenComplete((result, e) -> {
                    if (e != null) {
                        LOGGER.error("operation=publish," +
                                "msg=Failed to send MarketOperation message, " +
                                "error={}", e.getMessage(), e);
                    } else  {
                        LOGGER.info("operation=publish," +
                                "msg=Sent MarketOperation to market-changes, " +
                                "payload={}", marketOperation.toString());
                        LinkedHashMap<String,Object> value = result.value();
                        System.out.println(value.toString());
                        MarketOperationResult marketOperationResult = MarketOperationResultDeserializer.deserialize(value);
                        System.out.println(marketOperationResult.toString());
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
