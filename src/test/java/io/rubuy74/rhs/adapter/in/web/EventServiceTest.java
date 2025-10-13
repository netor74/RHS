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
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private WebClient.Builder webClientBuilder;
    
    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    
    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private RetryConfig retryConfig;

    private static final String FIRST_MOCK_EVENT_ID = "1";
    private static final String SECOND_MOCK_EVENT_ID = "2";
    private static final String FIRST_MOCK_EVENT_NAME = "Event 1";
    private static final String SECOND_MOCK_EVENT_NAME = "Event 2";
    private static final ZoneId zoneId = ZoneOffset.UTC;
    private static final List<Event> EXPECTED_EVENTS = List.of(
            new Event(FIRST_MOCK_EVENT_ID, FIRST_MOCK_EVENT_NAME, LocalDate
                    .parse("2025-12-01")
                    .atStartOfDay(zoneId)
                    .toInstant()
                    .toEpochMilli()
            ),
            new Event(SECOND_MOCK_EVENT_ID, SECOND_MOCK_EVENT_NAME, LocalDate
                    .parse("2025-12-02")
                    .atStartOfDay(zoneId)
                    .toInstant()
                    .toEpochMilli()
            )
    );

    private EventService createEventService() {
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        // the service configures a client connector; ensure the builder chaining continues
        when(webClientBuilder.clientConnector(any())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
        when(retryConfig.getMaxRetries()).thenReturn(0); // simplify tests: no retries
        when(retryConfig.getBackoffTimeMs()).thenReturn(1);
        return new EventService(webClientBuilder, "http://localhost:3000", retryConfig);
    }

    @Test
    @SuppressWarnings("unchecked")
    void getEvents_ShouldReturnEvents_WhenApiCallIsSuccessful() throws InterruptedException {
        EventService eventService = createEventService();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class)))
                .thenReturn(Mono.just(EXPECTED_EVENTS));

        StepVerifier.create(eventService.getEvents())
                .assertNext(actualEvents -> assertAll(
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
                            assertThat(actualEvents.get(1).getId()).isEqualTo(SECOND_MOCK_EVENT_ID);
                        },
                        () -> {
                            Assertions.assertNotNull(actualEvents);
                            assertThat(actualEvents.get(1).getName()).isEqualTo(SECOND_MOCK_EVENT_NAME);
                        }
                ))
                .verifyComplete();
    }

    @Test
    @SuppressWarnings("unchecked")
    void getEvents_ShouldErrorWithEventListingException_WhenApiReturns4xxError() throws InterruptedException {
        EventService eventService = createEventService();
        
        when(webClient.get()).thenReturn(requestHeadersUriSpec);

        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class)))
                .thenReturn(Mono.error(new EventListingException("Failed to retrieve events. Status400 BAD_REQUEST")));

        StepVerifier.create(eventService.getEvents())
                .expectErrorSatisfies(throwable -> {
                    assertThat(throwable).isInstanceOf(EventListingException.class);
                    assertThat(throwable.getMessage()).contains("Failed to retrieve events");
                })
                .verify();
    }

    @Test
    @SuppressWarnings("unchecked")
    void getEvents_ShouldErrorWithEventListingException_WhenApiReturns5xxError() throws InterruptedException {
        EventService eventService = createEventService();
        
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class)))
                .thenReturn(Mono.error(new EventListingException("MOS service error. Status: 500 INTERNAL_SERVER_ERROR")));

        StepVerifier.create(eventService.getEvents())
                .expectErrorSatisfies(throwable -> {
                    assertThat(throwable).isInstanceOf(EventListingException.class);
                    assertThat(throwable.getMessage()).contains("MOS service error");
                })
                .verify();
    }
}