package io.rubuy74.rhs.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
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

        assertAll(
            () -> assertThat(market).isNotNull(),
            () -> assertThat(market.toString()).contains("Market")
        );
    }

    static Stream<Arguments> invalidConstructorArgs() {
        return Stream.of(
            Arguments.of(null, MARKET_NAME, List.of()),
            Arguments.of(MARKET_ID, null, List.of()),
            Arguments.of(MARKET_ID, MARKET_NAME, null)
        );
    }

    @ParameterizedTest
    @MethodSource("invalidConstructorArgs")
    void constructor_ShouldThrow_WhenAnyArgumentIsNull(String id, String name, List<Selection> selections) {
        assertThrows(IllegalArgumentException.class, () -> new Market(id, name, selections));
    }
}


