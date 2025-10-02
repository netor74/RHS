package io.rubuy74.rhs.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.rubuy74.rhs.domain.MarketOperation;
import io.rubuy74.rhs.domain.http.MarketRequest;
import io.rubuy74.rhs.domain.http.OperationType;
import io.rubuy74.rhs.dto.EventDTO;
import io.rubuy74.rhs.port.out.MarketChangePublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class IngestMarketChangeTest {

    @Mock
    private MarketChangePublisher mockPublisher;

    @InjectMocks
    private IngestMarketChange ingestMarketChange;

    private MarketRequest createMarketRequest() {
        MarketRequest request = new MarketRequest();
        request.marketId = "market-123";
        request.marketName = "Over/Under 2.5 Goals";
        request.eventDTO = new EventDTO("evt-456", "Liverpool vs Arsenal", "2025-11-15");
        return request;
    }

    @Test
    void handle_ShouldDelegateToPublisher_WhenAddOperation() throws JsonProcessingException {
        MarketRequest marketRequest = createMarketRequest();
        MarketOperation operation = new MarketOperation(marketRequest, OperationType.ADD);

        ingestMarketChange.handle(operation);

        verify(mockPublisher).publish(operation);
    }

    @Test
    void handle_ShouldDelegateToPublisher_WhenEditOperation() throws JsonProcessingException {
        MarketRequest marketRequest = createMarketRequest();
        MarketOperation operation = new MarketOperation(marketRequest, OperationType.EDIT);

        ingestMarketChange.handle(operation);

        verify(mockPublisher).publish(operation);
    }

    @Test
    void handle_ShouldDelegateToPublisher_WhenDeleteOperation() throws JsonProcessingException {
        MarketRequest marketRequest = createMarketRequest();
        MarketOperation operation = new MarketOperation(marketRequest, OperationType.DELETE);

        ingestMarketChange.handle(operation);

        verify(mockPublisher).publish(operation);
    }

    @Test
    void handle_ShouldDelegateToPublisher_WithEmptyMarketRequest() throws JsonProcessingException {
        MarketRequest emptyRequest = new MarketRequest();
        MarketOperation operation = new MarketOperation(emptyRequest, OperationType.ADD);

        ingestMarketChange.handle(operation);

        verify(mockPublisher).publish(operation);
    }
}