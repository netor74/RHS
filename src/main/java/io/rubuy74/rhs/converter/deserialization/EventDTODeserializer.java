package io.rubuy74.rhs.converter.deserialization;

import io.rubuy74.rhs.dto.EventDTO;
import io.rubuy74.rhs.utils.ValidatorUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

public class EventDTODeserializer {
    private static final List<String> ATTRIBUTE_LIST = List.of("name", "date");

    private static boolean checkDate(Long date) {
        try  {
            return LocalDate.now()
                    .atStartOfDay(ZoneOffset.UTC)
                    .toInstant()
                    .toEpochMilli() <= date;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private static void checkEventValidity(Map<String,Object> rawPayload) {
        ValidatorUtils.checkArgument(
                rawPayload == null,
                "EventDTO payload is null",
                "deserialize_event_dto");
        ValidatorUtils.checkAttributeList(
                rawPayload,
                ATTRIBUTE_LIST);
        boolean validDate = checkDate((long) rawPayload.get("date"));
        ValidatorUtils.checkArgument(!validDate,
                "Event DTO Date is invalid",
                "deserialize_event_dto"
        );
    }

    public static EventDTO deserialize(Map<String,Object> rawPayload) {
        checkEventValidity(rawPayload); // throws exception if not valid

        String id = (String) rawPayload.get("id");
        String name = (String) rawPayload.get("name");
        Long date = (Long) rawPayload.get("date");
        return new EventDTO(id,name,date);
    }
}
