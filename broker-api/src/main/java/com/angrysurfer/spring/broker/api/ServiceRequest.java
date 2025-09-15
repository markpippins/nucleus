package com.angrysurfer.spring.broker.api;
import java.util.Map;
import java.util.UUID;

public record ServiceRequest(
        String service,
        String operation,
        Map<String, Object> params,
        String requestId) {

    public ServiceRequest(String service, String operation, Map<String, Object> params) {
        this(service, operation, params, UUID.randomUUID().toString());
    }

    public ServiceRequest(String service, String operation, Map<String, Object> params, String requestId) {
        this.service = service;
        this.operation = operation;
        this.params = params;
        this.requestId = requestId;
    }
}
