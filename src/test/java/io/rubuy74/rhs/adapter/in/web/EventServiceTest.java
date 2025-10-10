package io.rubuy74.rhs.adapter.in.web;

import io.rubuy74.rhs.domain.Event;
import io.rubuy74.rhs.exception.EventListingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @InjectMocks
    private EventService eventService;

    @Mock
    private RestClient restClient;
    
    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;
    
    @Mock
    private RestClient.RequestHeadersSpec requestHeadersSpec;
    
    @Mock 
    private RestClient.ResponseSpec responseSpec;

    @Test
    void getEvents_ShouldReturnEvents_WhenApiCallIsSuccessful() {
        List<Event> expectedEvents = List.of(
            new Event("1", "Mock Event 1", "2025-12-01"),
            new Event("2", "Mock Event 2", "2025-12-02")
        );
        
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(expectedEvents);

        List<Event> actualEvents = eventService.getEvents(restClient);
        assertThat(actualEvents).hasSize(2);
        assertThat(actualEvents.get(0).getId()).isEqualTo("1");
        assertThat(actualEvents.get(0).getName()).isEqualTo("Mock Event 1");
        assertThat(actualEvents.get(1).getId()).isEqualTo("2");
        assertThat(actualEvents.get(1).getName()).isEqualTo("Mock Event 2");
    }

    @Test
    void getEvents_ShouldThrowEventListingException_WhenApiReturns4xxError() {
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        
        when(responseSpec.onStatus(any(), any())).thenAnswer(invocation -> {
            throw new EventListingException("Failed to retrieve events. Status400 BAD_REQUEST");
        });
        assertThatThrownBy(() -> eventService.getEvents(restClient))
                .isInstanceOf(EventListingException.class)
                .hasMessageContaining("Failed to retrieve events");
    }

    @Test
    void getEvents_ShouldThrowEventListingException_WhenApiReturns5xxError() {
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        
        when(responseSpec.onStatus(any(), any())).thenAnswer(invocation -> {
            throw new EventListingException("MOS service error. Status: 500 INTERNAL_SERVER_ERROR");
        });
        assertThatThrownBy(() -> eventService.getEvents(restClient))
                .isInstanceOf(EventListingException.class)
                .hasMessageContaining("MOS service error");
    }
}