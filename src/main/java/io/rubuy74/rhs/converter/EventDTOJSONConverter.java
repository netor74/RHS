package io.rubuy74.rhs.converter;

import io.rubuy74.rhs.dto.EventDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class EventDTOJSONConverter {
    public static Logger logger = LoggerFactory.getLogger(EventDTOJSONConverter.class);
    public static EventDTO fromJson(Map<String,Object> rawPayload) {
        if (rawPayload == null) {
            logger.error("operation=parse_event_dto_json, " +
                    "msg=Raw payload is null");
            return null;
        }
        if (!rawPayload.containsKey("event")) {
            logger.error("operation=parse_event_dto_json, " +
                    "msg=Event is null");
            return null;
        }
        if (!rawPayload.containsKey("name")) {
            logger.error("operation=parse_event_dto_json, " +
                    "msg=Name is null");
            return null;
        }
        if (!rawPayload.containsKey("date")) {
            logger.error("operation=parse_event_dto_json, " +
                    "msg=Date is null");
            return null;
        }

        String id = (String) rawPayload.get("id");
        String name = (String) rawPayload.get("name");
        String date = (String) rawPayload.get("date");

        return new EventDTO(id,name,date);
    }
}
