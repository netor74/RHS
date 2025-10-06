package io.rubuy74.rhs.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MarketTest {
    private static final String MARKET_ID = "m1";
    private static final String MARKET_NAME = "Market 1";
    private static final String SELECTION_ID = "s1";
    private static final String SELECTION_NAME = "Sel 1";
    private static final double SELECTION_ODD = 2.0;

    @Test
    void constructor_ShouldSetFields_WhenValidArgsProvided() {
        List<Selection> selectionList = List.of(new Selection(SELECTION_ID, SELECTION_NAME, SELECTION_ODD));
        Market market = new Market(MARKET_ID, MARKET_NAME, selectionList);
        assertThat(market).isNotNull();
        assertThat(market.toString()).contains("Market");
    }

    @Test
    void constructor_ShouldThrow_WhenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new Market(null, MARKET_NAME, List.of()));
    }

    @Test
    void constructor_ShouldThrow_WhenNameIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new Market(MARKET_ID, null, List.of()));
    }

    @Test
    void constructor_ShouldThrow_WhenSelectionsIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new Market(MARKET_ID, MARKET_NAME, null));
    }
}


