package io.rubuy74.rhs.adapter.out.messaging;

import io.rubuy74.rhs.domain.MarketOperation;
import io.rubuy74.rhs.port.out.MarketChangePublisher;
import org.springframework.stereotype.Component;

@Component
public class KafkaMarketChangePublisher implements MarketChangePublisher {

    // TODO: Publish messages to Kafka
    @Override
    public void publish(MarketOperation marketOperation) {

    }
}
