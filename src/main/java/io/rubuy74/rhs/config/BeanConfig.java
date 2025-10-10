package io.rubuy74.rhs.config;

import io.rubuy74.rhs.application.IngestMarketChange;
import io.rubuy74.rhs.port.in.MarketChangeUseCase;
import io.rubuy74.rhs.port.out.MarketChangePublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    @Bean
    MarketChangeUseCase publishMarketUpdateUsecase(MarketChangePublisher marketChangePublisher) {
        return new IngestMarketChange(marketChangePublisher);
    }
}
