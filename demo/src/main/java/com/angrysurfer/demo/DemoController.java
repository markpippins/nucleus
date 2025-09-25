package com.angrysurfer.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// @CrossOrigin("*")
@RestController
@RequestMapping(path = "/api/demo")
public class DemoController {

    private static final Logger log = LoggerFactory.getLogger(DemoController.class);

    public DemoController() {
        log.info("DemoController initialized");
    }
    
    @RequestMapping(path = "/")
    public String hello() {
        log.info("Demo endpoint was called");
        return "Hello, World!";
    }

    @PostMapping(path = "/submit")
    public ResponseEntity<?> post() {
        log.info("Post endpoint was called");
        return ResponseEntity.ok("Post request received!");
    }

    @PostMapping(value = "/submitRequest", consumes = {"application/json"})
    public ResponseEntity<?> submitRequest(@RequestBody RequestPacket request) {
        log.info("Received request: id={}, name={}, message={}", request.getId(), request.getName(), request.getMessage());
        String response = "Request received with id: " + request.getId() + ", name: " + request.getName() + ", message: " + request.getMessage();
        return ResponseEntity.ok(response);
    }
}
