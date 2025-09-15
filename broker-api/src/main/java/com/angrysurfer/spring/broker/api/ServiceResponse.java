package com.angrysurfer.spring.broker.api;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record ServiceResponse<T>(
        boolean ok,
        T data,
        List<Map<String, Object>> errors,
        String requestId,
        Instant ts
        ) {

    public static <T> ServiceResponse<T> ok(T data, String requestId) {
        return new ServiceResponse<>(true, data, List.of(), requestId, Instant.now());
    }

    public static ServiceResponse<?> error(List<Map<String, Object>> errors, String requestId) {
        return new ServiceResponse<>(false, null, errors, requestId, Instant.now());
    }
}
