package io.rubuy74.rhs.adapter.in.web;

import io.rubuy74.rhs.domain.Event;
import io.rubuy74.rhs.exception.EventListingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventController.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EventService eventService;

    private static final List<Event> MOCK_EVENTS = List.of(
            new Event("1", "Event 1", LocalDate.parse("2025-10-10")),
            new Event("2", "Event 2", LocalDate.parse("2025-10-11"))
    );

    @Test
    void getEvents_ShouldReturnSuccessResponse_WhenServiceSucceeds() throws Exception {
        when(eventService.getEvents()).thenReturn(MOCK_EVENTS);
        mockMvc.perform(get("/api/v1/events"))
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
                .thenThrow(new EventListingException(errorMessage));
        mockMvc.perform(get("/api/v1/events"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.events").isArray())
                .andExpect(jsonPath("$.events").isEmpty());
    }

    @Test
    void getEvents_ShouldReturnInternalServerError_WhenGenericExceptionIsThrown() throws Exception {
        String errorMessage = "Something went wrong internally";
        when(eventService.getEvents())
                .thenThrow(new RuntimeException(errorMessage));
        mockMvc.perform(get("/api/v1/events"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.events").isArray())
                .andExpect(jsonPath("$.events").isEmpty());
    }

    @Test
    void getEvents_ShouldHandleNullExceptionMessage() throws Exception {
        when(eventService.getEvents())
                .thenThrow(new RuntimeException((String) null));
        mockMvc.perform(get("/api/v1/events"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value("Unknown error."))
                .andExpect(jsonPath("$.events").isArray())
                .andExpect(jsonPath("$.events").isEmpty());
    }
}