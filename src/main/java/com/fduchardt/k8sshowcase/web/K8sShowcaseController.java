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

    @GetMapping(path="/hostname")
    public String getHostname() {
        log.info("Queried hostname");
        return System.getenv("HOSTNAME");
    }

    @GetMapping(path="/compute/{iterations}")
    public String compute(@PathVariable int iterations) {
        log.info("Called compute with {} iterations", iterations);
        IntStream.range(0, iterations * 1000).parallel().forEach((i) -> Math.tan(Math.atan(Math.tan(Math.atan(i))))); ;
        return "done";
    }

    @GetMapping(path="/ram/{iterations}")
    public String ram(@PathVariable int iterations) {
        log.info("Called RAM with {} iterations", iterations);
        StringBuilder thousandRandomChars = new StringBuilder();
        IntStream.range(0, 1000).parallel().forEach((i) -> thousandRandomChars.append((int)(Math.random() * 256)));
        StringBuilder massiveString = new StringBuilder();
        IntStream.range(0, 1000 * iterations).forEach((i) -> massiveString.append(thousandRandomChars.toString()));
        return "done";
    }
}
