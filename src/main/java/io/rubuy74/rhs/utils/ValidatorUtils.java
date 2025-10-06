package io.rubuy74.rhs.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ValidatorUtils {
    private static final Logger logger = LoggerFactory.getLogger(ValidatorUtils.class);
    public static void checkAttributeList(
            Map<String,Object> rawPayload,
            List<String> attributeList,
            String operation
    ) {
        List<String> errorMessages = new ArrayList<>();
        attributeList.forEach(attribute -> {
            if(!rawPayload.containsKey(attribute)) {
                errorMessages.add("attribute '" + attribute + "' doesn't exist");
            }
        });

        if(!errorMessages.isEmpty()) {
            errorMessages.forEach((errorMessage) -> {
                logger.error("operation={}, msg={}",operation, errorMessage);
            });
            throw new IllegalArgumentException(errorMessages.toString());
        }
    }

    public static void checkArgument(boolean expression, String errorMessage, String operation) {
        if (!expression) {
            logger.error("operation=, {}" +
                    "msg={}",operation, errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
