package com.fduchardt.k8sshowcase.config;

import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.builders.*;
import springfox.documentation.spi.*;
import springfox.documentation.spring.web.plugins.*;
import springfox.documentation.swagger2.annotations.*;

import java.io.*;

@Configuration
@Slf4j
public class AppConfig {

    @Value("${storage.folder-path}")
    String storageFolderPath;

    @Bean
    public String storageFolder() {
        log.info("Ensure that storage folder does exist: {}", storageFolderPath);
        boolean fileExists = new File(storageFolderPath).exists();
        if (!fileExists) {
            throw new RuntimeException("Storage folder path not found:" + storageFolderPath);
        }
        return storageFolderPath;
    }
}
