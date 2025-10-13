package io.rubuy74.rhs.converter.deserialization;

import io.rubuy74.rhs.dto.EventDTO;
import io.rubuy74.rhs.utils.ValidatorUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

public class EventDTODeserializer {
    private static final List<String> ATTRIBUTE_LIST = List.of("name", "date");

    private static boolean checkDate(String date) {
        try  {
            LocalDate localDate = LocalDate.parse(date);
            return !localDate.isBefore(LocalDate.now());
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
        ValidatorUtils.checkArgument(
                !(
                        rawPayload.get("date") instanceof String &&
                        checkDate((String) rawPayload.get("date"))
                ),
                "Event DTO Date is invalid",
                "deserialize_event_dto"
        );
    }

    public static EventDTO deserialize(Map<String,Object> rawPayload) {
        checkEventValidity(rawPayload); // throws exception if not valid

        String id = (String) rawPayload.get("id");
        String name = (String) rawPayload.get("name");
        ZoneId zoneId = ZoneOffset.UTC;
        long epochMilliseconds = LocalDate
                .parse((String) rawPayload.get("date"))
                .atStartOfDay(zoneId)
                .toInstant()
                .toEpochMilli();
        System.out.println("epoch milliseconds: " + epochMilliseconds);
        return new EventDTO(id,name,epochMilliseconds);
    }
}
