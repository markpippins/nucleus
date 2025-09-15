package com.angrysurfer.spring;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.angrysurfer.spring.broker.spi.BrokerOperation;
import com.angrysurfer.spring.broker.spi.BrokerParam;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Service("userService")
public class UserService {

    public UserService() {
        super();
    }

    public record User(Long id, String email, String name) {}
    public record CreateUserReq(
            @Email String email,
            @NotBlank String name
    ) {}

    @BrokerOperation("getById")
    public ResponseEntity<User> getById(@BrokerParam("id") Long id) {
        // pretend lookup
        return ResponseEntity.ok(new User(id, "user"+id+"@acme.com", "User "+id));
    }

    @BrokerOperation("create")
    public ResponseEntity<Map<String, Object>> create(@Valid @BrokerParam("user") CreateUserReq req) {
        // pretend persistence:
        User created = new User(1001L, req.email(), req.name());
        return ResponseEntity.ok(Map.of("created", created));
    }
}
