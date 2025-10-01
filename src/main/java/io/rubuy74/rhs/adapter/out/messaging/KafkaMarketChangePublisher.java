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
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class KafkaMarketChangePublisher implements MarketChangePublisher {
    private static final Logger logger = LoggerFactory.getLogger(KafkaMarketChangePublisher.class);
    private static final String topic = "market-changes";
    private static final ObjectMapper mapper = new ObjectMapper() ;

    private Properties getKafkaProperties() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("acks", "1");
        properties.put("retries", 3);
        properties.put("key.serializer", StringSerializer.class.getName() );
        properties.put("value.serializer", ByteArraySerializer.class.getName());
        return properties;
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

        KafkaProducer<String, byte[]> producer = new KafkaProducer<>(getKafkaProperties());
        ProducerRecord<String, byte[]> producerRecord = new ProducerRecord<>(
                topic,
                marketOperation.operationType.toString(),
                payload
        );

        producer.send(producerRecord, (recordMetadata,e ) -> {
            if (e != null) {
                logger.error("Failed to send message: {}", e.getMessage());
            } else {
                logger.info(
                        "Send record to market-changes: {} \n Operation({}): {}",
                        recordMetadata,
                        marketOperation.operationType.toString(),
                        marketOperation.marketRequest.toString()
                );
            }
        });

        producer.close();
    }
}
