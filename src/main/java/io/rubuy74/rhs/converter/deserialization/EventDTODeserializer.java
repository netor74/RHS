package io.rubuy74.rhs.converter.deserialization;

import io.rubuy74.rhs.dto.EventDTO;
import io.rubuy74.rhs.utils.ValidatorUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class EventDTODeserializer {
    private static final List<String> ATTRIBUTE_LIST = List.of("name", "date");

    public static EventDTO deserialize(Map<String,Object> rawPayload) {
        ValidatorUtils.checkArgument(
                rawPayload != null,
                "EventDTO payload is null",
                "deserialize_event_dto");
        ValidatorUtils.checkAttributeList(
                rawPayload,
                ATTRIBUTE_LIST,
                "deserialize_event_dto");
        ValidatorUtils.checkArgument(
                rawPayload.get("date") instanceof LocalDate,
                "Event DTO Date is invalid",
                "deserialize_event_dto"
        );

        String id = (String) rawPayload.get("id");
        String name = (String) rawPayload.get("name");
        LocalDate date = LocalDate.parse((String) rawPayload.get("date"));

        return new EventDTO(id,name,date);
    }
}
