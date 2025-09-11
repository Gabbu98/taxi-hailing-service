package com.pickmeup.taxi_service.domain.utils;

import java.util.UUID;

public class StringUtils {
    public static String generateUniqueId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
