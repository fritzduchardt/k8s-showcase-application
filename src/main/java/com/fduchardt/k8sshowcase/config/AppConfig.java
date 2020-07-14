package com.fduchardt.k8sshowcase.config;

import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.*;
import springfox.documentation.builders.*;
import springfox.documentation.spi.*;
import springfox.documentation.spring.web.plugins.*;
import springfox.documentation.swagger2.annotations.*;

import java.io.*;

@Configuration
@Slf4j
public class AppConfig {

    @Value("${storage.storage-folder-path}")
    String storageFolderPath;

    @Value("${storage.config-folder-path}")
    String configFolderPath;

    @Value("${storage.validate-config}")
    boolean validateConfig;

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public String storageFolder() {
        log.info("Ensure that storage folder does exist: {}", storageFolderPath);
        boolean fileExists = new File(storageFolderPath).exists();
        if (!fileExists) {
            throw new RuntimeException("Storage folder path not found:" + storageFolderPath);
        }
        return storageFolderPath;
    }

    @Bean
    public boolean validateConfig() {
        if (validateConfig) {
            log.info("Validating config file exists: config.json");
            String configPath = configFolderPath + "/config.json";
            boolean fileExists = new File(configPath).exists();
            if (!fileExists) {
                throw new RuntimeException("Config file does not exist:" + configPath);
            }
            return true;
        }
        return false;
    }
}
