package com.angrysurfer.spring.upload.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileStorageProperties {

    @Value("${file.incoming-dir}")
    private String incomingDir;

    @Value("${file.processed-dir}")
    private String processedDir;

    @Value("${file.log-dir}")
    private String logDir;

    public String getIncomingDir() {
        return incomingDir;
    }

    public String getProcessedDir() {
        return processedDir;
    }

    public String getLogDir() {
        return logDir;
    }
}
