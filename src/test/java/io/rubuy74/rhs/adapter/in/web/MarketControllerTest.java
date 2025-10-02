package io.rubuy74.rhs.adapter.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.rubuy74.rhs.domain.MarketOperation;
import io.rubuy74.rhs.domain.http.MarketRequest;
import io.rubuy74.rhs.domain.http.OperationType;
import io.rubuy74.rhs.dto.EventDTO;
import io.rubuy74.rhs.port.in.MarketChangeUseCase;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MarketController.class)
class MarketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MarketChangeUseCase marketChangeUseCase;

    private static final EventDTO eventDTO = new EventDTO("evt-01", "Team A vs Team B", "2025-10-28");
    private static final MarketRequest MARKET_REQUEST = new MarketRequest(
            "1234",
            "MatchWinner",
            eventDTO,
            new ArrayList<>()
    );

    @Test
    void addMarkets_ShouldCallUseCaseWithAddOperation() throws Exception {
        String requestJson = objectMapper.writeValueAsString(MARKET_REQUEST);

        mockMvc.perform(post("/api/v1/market-change")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
        ArgumentCaptor<MarketOperation> captor = ArgumentCaptor.forClass(MarketOperation.class);
        verify(marketChangeUseCase).handle(captor.capture());

        MarketOperation capturedOperation = captor.getValue();
        assertThat(capturedOperation.getOperationType()).isEqualTo(OperationType.ADD);
        assertThat(capturedOperation.getMarketRequest().marketId).isEqualTo("12345");
        assertThat(capturedOperation.getMarketRequest().marketName).isEqualTo("Match Winner");
        assertThat(capturedOperation.getMarketRequest().eventDTO.getId()).isEqualTo("evt-01");
    }

    @Test
    void editMarkets_ShouldCallUseCaseWithEditOperation() throws Exception {
        String requestJson = objectMapper.writeValueAsString(MARKET_REQUEST);

        mockMvc.perform(put("/api/v1/market-change")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
        ArgumentCaptor<MarketOperation> captor = ArgumentCaptor.forClass(MarketOperation.class);
        verify(marketChangeUseCase).handle(captor.capture());

        MarketOperation capturedOperation = captor.getValue();
        assertThat(capturedOperation.getOperationType()).isEqualTo(OperationType.EDIT);
        assertThat(capturedOperation.getMarketRequest().marketId).isEqualTo("12345");
        assertThat(capturedOperation.getMarketRequest().marketName).isEqualTo("Match Winner");
        assertThat(capturedOperation.getMarketRequest().eventDTO.getName()).isEqualTo("Team A vs Team B");
    }

    @Test
    void deleteMarkets_ShouldCallUseCaseWithDeleteOperation() throws Exception {
        String requestJson = objectMapper.writeValueAsString(MARKET_REQUEST);

        mockMvc.perform(delete("/api/v1/market-change")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
        ArgumentCaptor<MarketOperation> captor = ArgumentCaptor.forClass(MarketOperation.class);
        verify(marketChangeUseCase).handle(captor.capture());

        MarketOperation capturedOperation = captor.getValue();
        assertThat(capturedOperation.getOperationType()).isEqualTo(OperationType.DELETE);
        assertThat(capturedOperation.getMarketRequest().marketId).isEqualTo("12345");
        assertThat(capturedOperation.getMarketRequest().eventDTO.getId()).isEqualTo("evt-01");
        assertThat(capturedOperation.getMarketRequest().eventDTO.getDate()).isEqualTo("2025-10-28");
    }
}