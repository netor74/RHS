package io.rubuy74.rhs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

import java.util.LinkedHashMap;

@Configuration
public class KafkaConfig {
    @Bean
    public ReplyingKafkaTemplate<String,byte[], LinkedHashMap<String,Object>> kafkaTemplate(
            ProducerFactory<String, byte[]> producerFactory,
            ConcurrentKafkaListenerContainerFactory<String, LinkedHashMap<String,Object>> kafkaListenerContainerFactory
    ) {
        ConcurrentMessageListenerContainer<String, LinkedHashMap<String,Object>> replyContainer =
                kafkaListenerContainerFactory.createContainer("market-changes-status");
        replyContainer.getContainerProperties().setGroupId("market-changes-status-group");

        return new ReplyingKafkaTemplate<>(producerFactory, replyContainer);
    }
}
