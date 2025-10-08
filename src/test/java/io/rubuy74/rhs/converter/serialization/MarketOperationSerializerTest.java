package io.rubuy74.rhs.converter.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.rubuy74.rhs.domain.MarketOperation;
import io.rubuy74.rhs.domain.Selection;
import io.rubuy74.rhs.domain.http.MarketRequest;
import io.rubuy74.rhs.domain.http.OperationType;
import io.rubuy74.rhs.dto.EventDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.InvalidObjectException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertAll;

class MarketOperationSerializerTest {

    private final MarketOperationSerializer serializer = new MarketOperationSerializer();
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    void serialize_ShouldReturnBytes_WhenValidObjectProvided() throws Exception {
        EventDTO eventDTO = new EventDTO("e1", "Game A vs Game B", LocalDate.parse("2026-11-20"));
        List<Selection> selections = List.of(
                new Selection("s1", "Team A", 1.8),
                new Selection("s2", "Team B", 2.0)
        );
        MarketRequest marketRequest = new MarketRequest("m1", "Match Result", eventDTO, selections);
        MarketOperation marketOperation = new MarketOperation(marketRequest, OperationType.ADD);

        byte[] serializedBytes = serializer.serialize(marketOperation);
        MarketOperation deserializedObject = mapper.readValue(serializedBytes, MarketOperation.class);

        assertAll(
                () -> assertThat(serializedBytes).isNotEmpty(),
                () -> assertThat(deserializedObject.getOperationType()).isEqualTo(OperationType.ADD),
                () -> assertThat(deserializedObject.getMarketRequest().marketId).isEqualTo("m1"),
                () -> assertThat(deserializedObject.getMarketRequest().eventDTO.getName()).isEqualTo("Game A vs Game B")
        );
    }

    @Test
    void serialize_ShouldThrowException_WhenObjectIsNull() {
        assertThrows(InvalidObjectException.class, () -> serializer.serialize(null));
    }
}