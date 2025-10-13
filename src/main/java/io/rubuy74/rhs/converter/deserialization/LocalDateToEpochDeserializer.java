package io.rubuy74.rhs.converter.deserialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneOffset;

public class LocalDateToEpochDeserializer extends JsonDeserializer<Long> {
    @Override
    public Long deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String dateAsString = p.getText();
        if (dateAsString == null || dateAsString.isEmpty()) {
            return null;
        }
        try {
            LocalDate localDate = LocalDate.parse(dateAsString);
            return localDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
        } catch (Exception e) {
            throw new IOException("operation=deserialize, " +
                    "msg=Failed to deserialize LocalDate string to Epoch long, " +
                    "err= ", e);
        }
    }
}