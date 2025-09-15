package com.angrysurfer.spring.upload.web;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.angrysurfer.spring.broker.api.ServiceRequest;
import com.angrysurfer.spring.broker.core.ServiceBroker;
import com.angrysurfer.spring.upload.service.FileService;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
@RequestMapping("/api/upload")
public class UploadController {

    private static final Logger log = LoggerFactory.getLogger(UploadController.class);

    private final FileService fileService;
    private final ServiceBroker broker;

    public UploadController(FileService fileService, ServiceBroker broker) {
        this.fileService = fileService;
        this.broker = broker;
        log.info("UploadController initialized");
    }

    @PostMapping(value = "/requestServiceWithFile", consumes = {"multipart/form-data"})
    public ResponseEntity<?> requestServiceWithFile(
            @RequestParam("service") String service,
            @RequestParam("operation") String operation,
            @RequestParam(value = "params", required = false) String paramsJson,
            @RequestPart("file") MultipartFile file) {

        log.info("Received file upload for service: {}, operation: {}, file: {}", service, operation, file.getOriginalFilename());

        Map<String, Object> params = new HashMap<>();
        if (paramsJson != null) {
            try {
                params = new ObjectMapper().readValue(paramsJson, Map.class);
                log.debug("Parsed params: {}", params);
            } catch (IOException e) {
                log.error("Invalid params JSON: {}", paramsJson, e);
                return ResponseEntity.badRequest().body("Invalid params JSON");
            }
        }

        try {
            Path path = fileService.saveUploadedFile(file);
            params.put("filePath", path);
            log.info("File saved at: {}", path);
            return broker.invoke(new ServiceRequest(service, operation, params, null));
        } catch (IOException e) {
            log.error("Error saving file: {}", file.getOriginalFilename(), e);
            return ResponseEntity.badRequest().body("Error saving file");
        }
    }
}
