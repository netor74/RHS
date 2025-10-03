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

import java.time.LocalDate;
import java.util.ArrayList;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class IngestMarketChangeTest {

    @Mock
    private MarketChangePublisher mockPublisher;

    @InjectMocks
    private IngestMarketChange ingestMarketChange;

    private static final EventDTO eventDTO = new EventDTO("evt-456", "Liverpool vs Arsenal", LocalDate.parse("2025-11-15"));

    private static final MarketRequest MARKET_REQUEST = new MarketRequest(
            "market-123",
            "Over/Under 2.5 Goals",
            eventDTO,
            new java.util.ArrayList<>());


    @Test
    void handle_ShouldDelegateToPublisher_WhenAddOperation() throws JsonProcessingException {
        MarketOperation operation = new MarketOperation(MARKET_REQUEST, OperationType.ADD);

        ingestMarketChange.handle(operation);

        verify(mockPublisher).publish(operation);
    }

    @Test
    void handle_ShouldDelegateToPublisher_WhenEditOperation() throws JsonProcessingException {
        MarketOperation operation = new MarketOperation(MARKET_REQUEST, OperationType.EDIT);

        ingestMarketChange.handle(operation);

        verify(mockPublisher).publish(operation);
    }

    @Test
    void handle_ShouldDelegateToPublisher_WhenDeleteOperation() throws JsonProcessingException {
        MarketOperation operation = new MarketOperation(MARKET_REQUEST, OperationType.DELETE);

        ingestMarketChange.handle(operation);

        verify(mockPublisher).publish(operation);
    }

    @Test
    void handle_ShouldDelegateToPublisher_WithEmptyMarketRequest() throws JsonProcessingException {
        MarketRequest emptyRequest = new MarketRequest("", "", null, new java.util.ArrayList<>());
        MarketOperation operation = new MarketOperation(emptyRequest, OperationType.ADD);

        ingestMarketChange.handle(operation);

        verify(mockPublisher).publish(operation);
    }
}