package com.angrysurfer.spring.broker.service;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.angrysurfer.spring.broker.api.ServiceRequest;
import com.angrysurfer.spring.broker.api.ServiceResponse;
import com.angrysurfer.spring.broker.spi.BrokerOperation;
import com.angrysurfer.spring.broker.spi.BrokerParam;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@Component
public class ServiceBroker {

    private static final Logger log = LoggerFactory.getLogger(ServiceBroker.class);

    private final ApplicationContext ctx;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    private record MethodKey(String service, String operation) {}
    private final Map<MethodKey, Method> methodCache = new ConcurrentHashMap<>();

    public ServiceBroker(ApplicationContext ctx, ObjectMapper objectMapper, Validator validator) {
        this.ctx = ctx;
        this.objectMapper = objectMapper;
        this.validator = validator;
        log.info("ServiceBroker initialized");
    }

    public ResponseEntity<?> invoke(ServiceRequest req) {
        log.info("Invoking service: {}, operation: {}, requestId: {}", req.service(), req.operation(), req.requestId());
        try {
            Object bean = resolveBean(req.service());
            Method method = resolveMethod(bean, req.operation());
            Object[] args = bindArgs(method, req.params(), req.requestId());

            Object result = method.invoke(bean, args);

            if (result instanceof ResponseEntity<?> re) {
                log.debug("Service returned ResponseEntity directly");
                return re;
            }
            // Optionally wrap in your standard envelope:
            log.debug("Service returned: {}", result);
            return ResponseEntity.ok(ServiceResponse.ok(result, req.requestId()));
        } catch (NoSuchElementException e) {
            log.warn("Not found: {}", e.getMessage());
            return badRequest("not_found", e.getMessage(), req.requestId());
        } catch (BrokerValidationException e) {
            log.warn("Validation error: {}", e.getErrors());
            return ResponseEntity.badRequest().body(
                    ServiceResponse.error(e.getErrors(), e.getRequestId()));
        } catch (IllegalArgumentException e) {
            log.warn("Binding error: {}", e.getMessage());
            return badRequest("binding_error", e.getMessage(), req.requestId());
        } catch (InvocationTargetException e) {
            Throwable cause = e.getTargetException();
            log.error("Service error: {}", cause.getMessage(), cause);
            return serverError("service_error", cause.getClass().getSimpleName() + ": " + cause.getMessage(), req.requestId());
        } catch (IllegalAccessException | RuntimeException e) {
            log.error("Broker error: {}", e.getMessage(), e);
            return serverError("broker_error", e.getMessage(), req.requestId());
        }
    }

    private Object resolveBean(String serviceName) {
        if (!StringUtils.hasText(serviceName)) {
            log.error("Missing 'service' name");
            throw new NoSuchElementException("Missing 'service' name.");
        }
        if (ctx.containsBean(serviceName)) {
            log.debug("Found bean by name: {}", serviceName);
            return ctx.getBean(serviceName);
        }
        // Try by type simple name (e.g., "UserService" -> bean)
        String[] beanNames = ctx.getBeanDefinitionNames();
        for (String name : beanNames) {
            Object bean = ctx.getBean(name);
            if (bean.getClass().getSimpleName().equalsIgnoreCase(serviceName)) {
                log.debug("Found bean by type simple name: {}", serviceName);
                return bean;
            }
        }
        log.error("Service bean not found: {}", serviceName);
        throw new NoSuchElementException("Service bean not found: " + serviceName);
    }

    private Method resolveMethod(Object bean, String operation) {
        if (!StringUtils.hasText(operation)) {
            log.error("Missing 'operation' name");
            throw new NoSuchElementException("Missing 'operation' name.");
        }
        MethodKey key = new MethodKey(bean.getClass().getName(), operation);
        Method cached = methodCache.get(key);
        if (cached != null) {
            log.debug("Using cached method for operation: {}", operation);
            return cached;
        }

        // Only methods annotated with @BrokerOperation are exposed.
        List<Method> candidates = Arrays.stream(bean.getClass().getMethods())
                .filter(m -> m.isAnnotationPresent(BrokerOperation.class))
                .filter(m -> {
                    String alias = m.getAnnotation(BrokerOperation.class).value();
                    return operation.equals(alias) || operation.equals(m.getName());
                })
                .toList();

        if (candidates.isEmpty()) {
            log.error("Operation not exposed: {}", operation);
            throw new NoSuchElementException("Operation not exposed: " + operation);
        }
        if (candidates.size() > 1) {
            // Prefer exact alias match
            Optional<Method> exact = candidates.stream()
                    .filter(m -> operation.equals(m.getAnnotation(BrokerOperation.class).value()))
                    .findFirst();
            Method chosen = exact.orElse(candidates.get(0));
            methodCache.put(key, chosen);
            log.debug("Multiple candidates, using: {}", chosen.getName());
            return chosen;
        }
        Method method = candidates.get(0);
        methodCache.put(key, method);
        log.debug("Resolved method: {}", method.getName());
        return method;
    }

    private Object[] bindArgs(Method method, Map<String, Object> params, String requestId) {
        params = (params == null) ? Map.of() : params;
        Parameter[] ps = method.getParameters();
        Object[] bound = new Object[ps.length];

        List<Map<String, Object>> violations = new ArrayList<>();

        for (int i = 0; i < ps.length; i++) {
            Parameter p = ps[i];
            Class<?> pt = p.getType();

            // Allow context injection without being in params:
            if (Principal.class.isAssignableFrom(pt)) {
                bound[i] = null; // Let a HandlerMethodArgumentResolver variant inject if you extend; keep null for now.
                continue;
            }
            if (jakarta.servlet.http.HttpServletRequest.class.isAssignableFrom(pt)) {
                bound[i] = null;
                continue;
            }

            // Name to lookup in params:
            BrokerParam paramAnnotation = p.getAnnotation(BrokerParam.class);
            String name = (paramAnnotation != null && org.springframework.util.StringUtils.hasText(paramAnnotation.value()))
                    ? paramAnnotation.value()
                    : p.getName(); // requires -parameters at compile to retain names

            boolean required = (paramAnnotation == null) || paramAnnotation.required();

            Object raw = params.get(name);
            if (raw == null && required) {
                log.warn("Missing required parameter: {}", name);
                throw new IllegalArgumentException("Missing required parameter: " + name);
            }

            Object converted = objectMapper.convertValue(raw, objectMapper.constructType(p.getParameterizedType()));

            // Manual validation for @Valid parameters (method-level programmatic):
            if (hasAnnotation(p, jakarta.validation.Valid.class) && converted != null) {
                Set<ConstraintViolation<Object>> errs = validator.validate(converted);
                if (!errs.isEmpty()) {
                    errs.stream().map(cv -> Map.<String, Object>of(
                            "param", name,
                            "path", cv.getPropertyPath().toString(),
                            "msg", cv.getMessage()
                    )).forEach(violations::add);
                }
            }

            bound[i] = converted;
        }

        if (!violations.isEmpty()) {
            log.warn("Validation violations: {}", violations);
            throw new BrokerValidationException(violations, requestId);
        }
        return bound;
    }

    private boolean hasAnnotation(Parameter p, Class<? extends Annotation> a) {
        for (Annotation an : p.getAnnotations()) {
            if (an.annotationType().equals(a)) return true;
        }
        return false;
    }

    private ResponseEntity<?> badRequest(String code, String msg, String requestId) {
        log.warn("Bad request [{}]: {}", code, msg);
        return ResponseEntity.badRequest().body(ServiceResponse.error(
                List.of(Map.of("code", code, "message", msg)), requestId));
    }

    private ResponseEntity<?> serverError(String code, String msg, String requestId) {
        log.error("Server error [{}]: {}", code, msg);
        return ResponseEntity.internalServerError().body(ServiceResponse.error(
                List.of(Map.of("code", code, "message", msg)), requestId));
    }
}