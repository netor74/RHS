package io.rubuy74.rhs.adapter.in.web;

import io.rubuy74.rhs.config.property.RetryConfig;
import io.rubuy74.rhs.domain.Event;
import io.rubuy74.rhs.exception.EventListingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private RestClient.Builder restClientBuilder;
    
    @Mock
    private RestClient restClient;
    
    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;
    
    @Mock
    private RestClient.RequestHeadersSpec requestHeadersSpec;
    
    @Mock 
    private RestClient.ResponseSpec responseSpec;

    @Mock
    private RetryConfig retryConfig;

    private static final String FIRST_MOCK_EVENT_ID = "1";
    private static final String SECOND_MOCK_EVENT_ID = "2";
    private static final String FIRST_MOCK_EVENT_NAME = "Event 1";
    private static final String SECOND_MOCK_EVENT_NAME = "Event 2";
    private static final List<Event> EXPECTED_EVENTS = List.of(
            new Event(FIRST_MOCK_EVENT_ID, FIRST_MOCK_EVENT_NAME, LocalDate.parse("2025-12-01")),
            new Event(SECOND_MOCK_EVENT_ID, SECOND_MOCK_EVENT_NAME, LocalDate.parse("2025-12-02"))
    );

    private EventService createEventService() {
        when(restClientBuilder.baseUrl(anyString())).thenReturn(restClientBuilder);
        when(restClientBuilder.build()).thenReturn(restClient);
        return new EventService(restClientBuilder, "http://localhost:3000",retryConfig);
    }

    @Test
    @SuppressWarnings("unchecked")
    void getEvents_ShouldReturnEvents_WhenApiCallIsSuccessful() throws InterruptedException {
        EventService eventService = createEventService();

        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(EXPECTED_EVENTS);

        List<Event> actualEvents = eventService.getEvents();
        assertAll(
                () -> assertThat(actualEvents).hasSize(2),
                () -> {
                    Assertions.assertNotNull(actualEvents);
                    assertThat(actualEvents.getFirst().getId()).isEqualTo(FIRST_MOCK_EVENT_ID);
                },
                () -> {
                    Assertions.assertNotNull(actualEvents);
                    assertThat(actualEvents.getFirst().getName()).isEqualTo(FIRST_MOCK_EVENT_NAME);
                },
                () -> {
                    Assertions.assertNotNull(actualEvents);
                    assertThat(actualEvents.getFirst().getId()).isEqualTo(SECOND_MOCK_EVENT_ID);
                },
                () -> {
                    Assertions.assertNotNull(actualEvents);
                    assertThat(actualEvents.getFirst().getName()).isEqualTo(SECOND_MOCK_EVENT_NAME);
                }
        );




    }

    @Test
    @SuppressWarnings("unchecked")
    void getEvents_ShouldThrowEventListingException_WhenApiReturns4xxError() {
        EventService eventService = createEventService();
        
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        
        when(responseSpec.onStatus(any(), any())).thenAnswer(invocation -> {
            throw new EventListingException("Failed to retrieve events. Status400 BAD_REQUEST");
        });
        assertThatThrownBy(eventService::getEvents)
                .isInstanceOf(EventListingException.class)
                .hasMessageContaining("Failed to retrieve events");
    }

    @Test
    @SuppressWarnings("unchecked")
    void getEvents_ShouldThrowEventListingException_WhenApiReturns5xxError() {
        EventService eventService = createEventService();
        
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        
        when(responseSpec.onStatus(any(), any())).thenAnswer(invocation -> {
            throw new EventListingException("MOS service error. Status: 500 INTERNAL_SERVER_ERROR");
        });
        assertThatThrownBy(eventService::getEvents)
                .isInstanceOf(EventListingException.class)
                .hasMessageContaining("MOS service error");
    }
}