package io.rubuy74.rhs.domain;

import io.rubuy74.rhs.domain.http.MarketRequest;
import io.rubuy74.rhs.domain.http.OperationType;
import io.rubuy74.rhs.dto.EventDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MarketOperationTest {
    private static final String EVENT_ID = "e1";
    private static final String EVENT_NAME = "Match";
    private static final java.time.LocalDate EVENT_DATE = java.time.LocalDate.parse("2025-12-01");
    private static final String MARKET_ID = "m1";
    private static final String MARKET_NAME = "Market 1";

    @Test
    void constructor_ShouldSetFields_WhenValidArgsProvided() {
        EventDTO eventDTO = new EventDTO(EVENT_ID, EVENT_NAME, EVENT_DATE);
        MarketRequest req = new MarketRequest(MARKET_ID, MARKET_NAME, eventDTO, new ArrayList<>());
        MarketOperation op = new MarketOperation(req, OperationType.ADD);

        assertThat(op.getMarketRequest()).isSameAs(req);
        assertThat(op.getOperationType()).isEqualTo(OperationType.ADD);
        assertThat(op.toString()).contains("operationType=ADD");
    }

    @Test
    void constructor_ShouldThrow_WhenMarketRequestIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new MarketOperation(null, OperationType.ADD));
    }

    @Test
    void constructor_ShouldThrow_WhenOperationTypeIsNull() {
        EventDTO eventDTO = new EventDTO(EVENT_ID, EVENT_NAME, EVENT_DATE);
        MarketRequest req = new MarketRequest(MARKET_ID, MARKET_NAME, eventDTO, new ArrayList<>());
        assertThrows(IllegalArgumentException.class, () -> new MarketOperation(req, null));
    }
}


