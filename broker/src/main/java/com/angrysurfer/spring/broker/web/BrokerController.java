package com.angrysurfer.spring.broker.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.angrysurfer.spring.broker.api.ServiceRequest;
import com.angrysurfer.spring.broker.core.ServiceBroker;

// @CrossOrigin("*")
@RestController
@RequestMapping("/api/broker")
public class BrokerController {

    private static final Logger log = LoggerFactory.getLogger(BrokerController.class);

    private final ServiceBroker broker;

    // final private FileService fileService;

    public BrokerController(ServiceBroker broker) {
        this.broker = broker;
        log.info("BrokerController initialized");
    }

    // public ServiceController(ServiceBroker broker, FileService fileService) {
    //     this.broker = broker;
    //     this.fileService = fileService;
    //     log.info("ServiceController initialized");
    // }

    @PostMapping(value = "/requestService", consumes = {"application/json"})
    public ResponseEntity<?> requestService(@RequestBody ServiceRequest request) {
        log.debug("Received request: {}", request);
        return broker.invoke(request);
    }

    // @PostMapping(value = "/requestServiceWithFile", consumes = {"multipart/form-data"})
    // public ResponseEntity<?> requestServiceWithFile(
    //         @RequestParam("service") String service,
    //         @RequestParam("operation") String operation,
    //         @RequestParam(value = "params", required = false) String paramsJson,
    //         @RequestPart("file") MultipartFile file) {

    //     log.info("Received file upload for service: {}, operation: {}, file: {}", service, operation, file.getOriginalFilename());

    //     Map<String, Object> params = new HashMap<>();
    //     if (paramsJson != null) {
    //         try {
    //             params = new ObjectMapper().readValue(paramsJson, Map.class);
    //             log.debug("Parsed params: {}", params);
    //         } catch (IOException e) {
    //             log.error("Invalid params JSON: {}", paramsJson, e);
    //             return ResponseEntity.badRequest().body("Invalid params JSON");
    //         }
    //     }

    //     try {
    //         Path path = fileService.saveUploadedFile(file);
    //         params.put("filePath", path);
    //         log.info("File saved at: {}", path);
    //         return broker.invoke(new ServiceRequest(service, operation, params, null));
    //     } catch (IOException e) {
    //         log.error("Error saving file: {}", file.getOriginalFilename(), e);
    //         return ResponseEntity.badRequest().body("Error saving file");
    //     }
    // }
}
