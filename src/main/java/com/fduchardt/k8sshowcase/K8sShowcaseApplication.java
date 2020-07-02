package com.fduchardt.k8sshowcase;

import lombok.extern.slf4j.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;

@Slf4j
@SpringBootApplication
public class K8sShowcaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(K8sShowcaseApplication.class, args);
    }
}
