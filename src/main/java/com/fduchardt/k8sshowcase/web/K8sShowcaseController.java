package com.fduchardt.k8sshowcase.web;

import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.*;
import java.util.stream.*;

@RequiredArgsConstructor
@RestController
@Slf4j
public class K8sShowcaseController {

    @GetMapping(path="/env/{envVariable}")
    public String getEnvVariable(@PathVariable String envVariable) {
        return System.getenv(envVariable);
    }

    @GetMapping(path="/list/{folderPath}")
    public String getFolder(@PathVariable String folderPath) throws IOException {
        log.info("list folder: {}", folderPath);
        Stream<Path> list = Files.list(Path.of(File.separatorChar + folderPath));
        return list.map(p -> p.toFile().getPath() + "\n").collect(Collectors.joining());
    }

    @GetMapping(path="/content/{filePath}/{fileName}")
    public String getContent(@PathVariable String filePath, @PathVariable String fileName) throws IOException {
        String completeFilePath = File.separatorChar + filePath + File.separatorChar + fileName;
        log.info("content file: {}", completeFilePath);
        return Files.readString(Path.of(completeFilePath));
    }

    @GetMapping(path="/hostname")
    public String getHostname() {
        log.info("Queried hostname");
        return System.getenv("HOSTNAME");
    }

    @GetMapping(path="/cpu/{iterations}")
    public String cpu(@PathVariable int iterations) {
        log.info("Called compute with {} iterations", iterations);
        IntStream.range(0, iterations * 1000).parallel().forEach((i) -> Math.tan(Math.atan(Math.tan(Math.atan(i))))); ;
        return "done";
    }

    @GetMapping(path="/stackoverflow")
    public void stackOverFlow() {
        log.info("Causing Stackoverflow");
        stackOverFlow();
    }

    @GetMapping(path="/memory/{megabytes}")
    public String memory(@PathVariable int megabytes) throws InterruptedException {
        log.info("Called memory with {} megabytes", megabytes);
        StringBuilder thousandRandomChars = new StringBuilder();
        IntStream.range(0, 1000).parallel().forEach((i) -> thousandRandomChars.append((int)(Math.random() * 256)));
        StringBuilder massiveString = new StringBuilder();
        for(int i = 0; i < megabytes; i++) {
            massiveString.append(thousandRandomChars.toString());
            Thread.sleep(10);

        }
        return "done";
    }

    @GetMapping(path="/space/{megabytes}")
    public String space(@PathVariable int megabytes) throws IOException {
        log.info("Called space with {} megabytes", megabytes);
        StringBuilder thousandRandomChars = new StringBuilder();
        IntStream.range(0, 1000).parallel().forEach((i) -> thousandRandomChars.append((int)(Math.random() * 256)));
        StringBuilder massiveString = new StringBuilder();
        FileOutputStream fileOutputStream = new FileOutputStream(new File("giga-file"));
        for(int i = 0; i < megabytes; i++) {
            massiveString.append(thousandRandomChars);
            fileOutputStream.write(massiveString.toString().getBytes());
        }
        fileOutputStream.close();
        return "done";
    }
}
