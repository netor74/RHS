package io.rubuy74.rhs.adapter.in.web;

import io.rubuy74.rhs.domain.Event;
import io.rubuy74.rhs.exception.EventListingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

@WebMvcTest(EventController.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EventService eventService;

    private static final ZoneId zoneId = ZoneOffset.UTC;
    private static final List<Event> MOCK_EVENTS = List.of(
            new Event("1", "Event 1", LocalDate
                    .parse("2025-10-10")
                    .atStartOfDay(zoneId)
                    .toInstant()
                    .toEpochMilli()
            ),
            new Event("2", "Event 2", LocalDate
                    .parse("2025-10-11")
                    .atStartOfDay(zoneId)
                    .toInstant()
                    .toEpochMilli()
            )
    );

    @Test
    void getEvents_ShouldReturnSuccessResponse_WhenServiceSucceeds() throws Exception {
        when(eventService.getEvents()).thenReturn(reactor.core.publisher.Mono.just(MOCK_EVENTS));
        var mvcResult = mockMvc.perform(get("/api/v1/events"))
                .andExpect(request().asyncStarted())
                .andReturn();
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value(""))
                .andExpect(jsonPath("$.events").isArray())
                .andExpect(jsonPath("$.events[0].id").value("1"))
                .andExpect(jsonPath("$.events[0].name").value("Event 1"))
                .andExpect(jsonPath("$.events[1].id").value("2"))
                .andExpect(jsonPath("$.events[1].name").value("Event 2"));
    }

    @Test
    void getEvents_ShouldReturnServiceUnavailable_WhenEventListingExceptionIsThrown() throws Exception {
        String errorMessage = "Failed to retrieve events. Status400 BAD_REQUEST";
        when(eventService.getEvents())
                .thenReturn(reactor.core.publisher.Mono.error(new EventListingException(errorMessage)));
        var mvcResult = mockMvc.perform(get("/api/v1/events"))
                .andExpect(request().asyncStarted())
                .andReturn();
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.events").isArray())
                .andExpect(jsonPath("$.events").isEmpty());
    }

    @Test
    void getEvents_ShouldReturnInternalServerError_WhenGenericExceptionIsThrown() throws Exception {
        when(eventService.getEvents())
                .thenReturn(reactor.core.publisher.Mono.error(new RuntimeException("Unknown Internal Server Error")));
        var mvcResult = mockMvc.perform(get("/api/v1/events"))
                .andExpect(request().asyncStarted())
                .andReturn();
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value("Unknown Internal Server Error: Unknown Internal Server Error"))
                .andExpect(jsonPath("$.events").isArray())
                .andExpect(jsonPath("$.events").isEmpty());
    }

    @Test
    void getEvents_ShouldHandleNullExceptionMessage() throws Exception {
        when(eventService.getEvents())
                .thenReturn(reactor.core.publisher.Mono.error(new RuntimeException((String) null)));
        var mvcResult = mockMvc.perform(get("/api/v1/events"))
                .andExpect(request().asyncStarted())
                .andReturn();
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value("Unknown Internal Server Error: null"))
                .andExpect(jsonPath("$.events").isArray())
                .andExpect(jsonPath("$.events").isEmpty());
    }
}