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

    private MarketRequest createMarketRequest() {
        MarketRequest request = new MarketRequest();
        request.marketId = "12345";
        request.marketName = "Match Winner";
        request.eventDTO = new EventDTO("evt-01", "Team A vs Team B", "2025-10-28");
        return request;
    }

    @Test
    void addMarkets_ShouldCallUseCaseWithAddOperation() throws Exception {
        MarketRequest request = createMarketRequest();
        String requestJson = objectMapper.writeValueAsString(request);

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
        MarketRequest request = createMarketRequest();
        String requestJson = objectMapper.writeValueAsString(request);

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
        MarketRequest request = createMarketRequest();
        String requestJson = objectMapper.writeValueAsString(request);

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