package io.rubuy74.rhs.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EventTest {
    private static final String EVENT_ID = "1";
    private static final String EVENT_NAME = "Name";
    private static final java.time.LocalDate EVENT_DATE = java.time.LocalDate.parse("2025-12-01");

    @Test
    void constructor_ShouldSetFields_WhenValidArgsProvided() {
        LocalDate date = EVENT_DATE;
        Event event = new Event(EVENT_ID, EVENT_NAME, date);

        assertThat(event.getId()).isEqualTo(EVENT_ID);
        assertThat(event.getName()).isEqualTo(EVENT_NAME);
        assertThat(event.getDate()).isEqualTo(date);
        assertThat(event.toString()).contains("id=" + EVENT_ID).contains(EVENT_NAME);
    }

    @Test
    void constructor_ShouldThrow_WhenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new Event(null, EVENT_NAME, EVENT_DATE));
    }

    @Test
    void constructor_ShouldThrow_WhenNameIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new Event(EVENT_ID, null, EVENT_DATE));
    }

    @Test
    void constructor_ShouldThrow_WhenDateIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new Event(EVENT_ID, EVENT_NAME, null));
    }
}


