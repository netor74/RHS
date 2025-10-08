package io.rubuy74.rhs.converter.deserialization;

import io.rubuy74.rhs.domain.Selection;
import io.rubuy74.rhs.utils.ValidatorUtils;

import java.util.List;
import java.util.Map;

public class SelectionDeserializer {
    private static final List<String> ATTRIBUTE_LIST = List.of("id","name","odd");

    public static Selection deserialize(Map<String,Object> rawPayload) {
        ValidatorUtils.checkArgument(
                rawPayload == null,
                "Selection payload is null",
                "deserialize_selection");
        ValidatorUtils.checkAttributeList(
                rawPayload,
                ATTRIBUTE_LIST
                );
        String id = (String) rawPayload.get("id");
        String name = (String) rawPayload.get("name");
        Double odd = (Double) rawPayload.get("odd");
        return new Selection(id, name, odd);
    }
}
