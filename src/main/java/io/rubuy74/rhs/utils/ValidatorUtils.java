package io.rubuy74.rhs.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ValidatorUtils {


    public static List<String> checkAttributeList(
            Map<String,Object> rawPayload,
            List<String> attributeList
    ) {
        List<String> messages = new ArrayList<>();
        attributeList.forEach(attribute -> {
            if(!rawPayload.containsKey(attribute)) {
                messages.add("attribute '" + attribute + "' doesn't exist");
            }
        });
        return messages;
    }

    public static void checkArgument(boolean expression, String errorMessage) {
        if (!expression) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
