package io.rubuy74.rhs.converter;

import io.rubuy74.rhs.dto.EventDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class EventDTOJSONConverter {
    public static Logger logger = LoggerFactory.getLogger(EventDTOJSONConverter.class);
    public static EventDTO fromJson(Map<String,Object> rawPayload) {
        if (rawPayload == null) {
            logger.error("raw payload is null");
            return null;
        }
        if (!rawPayload.containsKey("event")) {
            logger.error("event is null");
            return null;
        }
        if (!rawPayload.containsKey("name")) {
            logger.error("name is null");
            return null;
        }
        if (!rawPayload.containsKey("date")) {
            logger.error("date is null");
            return null;
        }

        String id = (String) rawPayload.get("id");
        String name = (String) rawPayload.get("name");
        String date = (String) rawPayload.get("date");

        return new EventDTO(id,name,date);
    }
}
