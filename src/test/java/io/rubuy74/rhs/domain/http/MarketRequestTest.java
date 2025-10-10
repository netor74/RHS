package io.rubuy74.rhs.domain.http;

import io.rubuy74.rhs.dto.EventDTO;
import io.rubuy74.rhs.domain.Selection;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class MarketRequestTest {
    private static final String MARKET_ID = "m1";
    private static final String MARKET_NAME = "Market 1";
    private static final String EVENT_ID = "e1";
    private static final String EVENT_NAME = "Match";
    private static final LocalDate EVENT_DATE = LocalDate.parse("2025-12-01");

    @Test
    void constructor_ShouldSetFieldsAndTimestamp() {
        EventDTO eventDTO = new EventDTO(EVENT_ID, EVENT_NAME, EVENT_DATE);
        MarketRequest req = new MarketRequest(MARKET_ID, MARKET_NAME, eventDTO, new ArrayList<Selection>());
        assertThat(req.marketId).isEqualTo(MARKET_ID);
        assertThat(req.marketName).isEqualTo(MARKET_NAME);
        assertThat(req.eventDTO).isSameAs(eventDTO);
        assertThat(req.timestamp).isNotNull();
        assertThat(req.selections).isNotNull();
    }
}


