package io.rubuy74.rhs.converter;

import io.rubuy74.rhs.dto.EventDTO;
import io.rubuy74.rhs.utils.ValidatorUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class EventDTOJSONConverter {
    private static final List<String> ATTRIBUTE_LIST = List.of("event", "name", "date");

    public static EventDTO fromJson(Map<String,Object> rawPayload) {
        ValidatorUtils.checkArgument(
                rawPayload != null,
                "EventDTO payload is null",
                "deserialize_event_dto");
        ValidatorUtils.checkAttributeList(
                rawPayload,
                ATTRIBUTE_LIST,
                "deserialize_event_dto");

        String id = (String) rawPayload.get("id");
        String name = (String) rawPayload.get("name");
        LocalDate date = LocalDate.parse((String) rawPayload.get("date"));

        return new EventDTO(id,name,date);
    }
}
