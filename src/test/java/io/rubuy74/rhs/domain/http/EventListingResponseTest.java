package io.rubuy74.rhs.domain.http;

import io.rubuy74.rhs.domain.Event;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EventListingResponseTest {
    private static final String EVENT_ID = "1";
    private static final String EVENT_NAME = "N";
    private static final LocalDate EVENT_DATE = LocalDate.parse("2025-12-01");
    private static final String MESSAGE = "";

    @Test
    void record_ShouldExposeFields() {
        Event evt = new Event(EVENT_ID, EVENT_NAME, EVENT_DATE);
        EventListingResponse res = new EventListingResponse(Status.SUCCESS, MESSAGE, List.of(evt));
        assertThat(res.status()).isEqualTo(Status.SUCCESS);
        assertThat(res.message()).isEqualTo(MESSAGE);
        assertThat(res.events()).containsExactly(evt);
    }
}


