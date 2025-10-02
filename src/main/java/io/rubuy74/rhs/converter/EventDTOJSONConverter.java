package io.rubuy74.rhs.converter;

import io.rubuy74.rhs.dto.EventDTO;

import java.util.Map;

public class EventDTOJSONConverter {
    public static EventDTO fromJson(Map<String,Object> rawPayload) {
        String id = (String) rawPayload.get("id");
        String name = (String) rawPayload.get("name");
        String date = (String) rawPayload.get("date");

        return new EventDTO(id,name,date);
    }
}
