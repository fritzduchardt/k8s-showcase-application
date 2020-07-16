package com.fduchardt.k8sshowcase.web;

import com.google.common.base.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.apache.commons.io.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.*;

import java.io.*;
import java.nio.file.*;
import java.util.stream.*;

@RequiredArgsConstructor
@RestController
@Slf4j
public class WebController {

    @Value("${service.url}")
    String serviceUrl;

    @Autowired
    RestTemplate restTemplate;

    @GetMapping(path = "/env/{envVariable}")
    public String getEnvVariable(@PathVariable String envVariable) {
        return System.getenv(envVariable);
    }

    @GetMapping(path = "/list/{folderPath}")
    public String getFolder(@PathVariable String folderPath) throws IOException {
        log.info("list folder: {}", folderPath);
        Stream<Path> list = Files.list(Path.of(File.separatorChar + folderPath));
        return list.map(p -> p.toFile().getPath() + "\n").collect(Collectors.joining());
    }

    @GetMapping(path = "/content/{filePath}/{fileName}")
    public String getContent(@PathVariable String filePath, @PathVariable String fileName) throws IOException {
        String completeFilePath = File.separatorChar + filePath + File.separatorChar + fileName;
        log.info("content file: {}", completeFilePath);
        return Files.readString(Path.of(completeFilePath));
    }

    @GetMapping(path = "/hostname")
    public String getHostname() {
        log.info("Queried hostname");
        return System.getenv("HOSTNAME");
    }

    @GetMapping(path = "/cpu/{iterations}")
    public String cpu(@PathVariable int iterations) {
        log.info("Called compute with {} iterations", iterations);
        IntStream.range(0, iterations * 1000).parallel().forEach((i) -> Math.tan(Math.atan(Math.tan(Math.atan(i)))));
        ;
        return "done";
    }

    @GetMapping(path = "/stackoverflow")
    public void stackOverFlow() {
        log.info("Causing Stackoverflow");
        stackOverFlow();
    }

    @GetMapping(path = "/memory/{megabytes}")
    public String memory(@PathVariable int megabytes) throws InterruptedException {
        log.info("Called memory with {} megabytes", megabytes);
        StringBuilder thousandRandomChars = new StringBuilder();
        IntStream.range(0, 1000).parallel().forEach((i) -> thousandRandomChars.append((int) (Math.random() * 256)));
        StringBuilder massiveString = new StringBuilder();
        for (int i = 0; i < megabytes; i++) {
            massiveString.append(thousandRandomChars.toString());
            Thread.sleep(10);

        }
        return "done";
    }

    @GetMapping(path = "/space/{hundredMegabytes}")
    public String space(@PathVariable int hundredMegabytes) throws IOException, InterruptedException {
        log.info("Called space with {} hundred-megabytes", hundredMegabytes);
        for (int i = 0; i < hundredMegabytes; i++) {
            FileOutputStream fileOutputStream = new FileOutputStream(new File("/tmp/space-" + i));
            InputStream resourceAsStream = this.getClass().getResourceAsStream("/100MB.bin");
            IOUtils.copy(resourceAsStream, fileOutputStream);
            fileOutputStream.close();
            resourceAsStream.close();
            Thread.sleep(100);
        }
        return "done";
    }

    @GetMapping(path = "/forward/{path}")
    public String forward(@PathVariable  String path) {
        String forwardUrl = "http://" + serviceUrl + "/" + path;
        log.info("Forwarding to url {}", forwardUrl);
        Stopwatch sw = Stopwatch.createStarted();
        String result = restTemplate.getForObject(forwardUrl, String.class);
        sw.stop();
        return result;
    }

    @GetMapping(path = "/forward/{path1}/{path2}")
    public String forward(@PathVariable String path1, @PathVariable  String path2) {
        String forwardUrl = "http://" + serviceUrl + "/" + path1 + "/" + path2;
        log.info("Forwarding to url {}", forwardUrl);
        Stopwatch sw = Stopwatch.createStarted();
        String result = restTemplate.getForObject(forwardUrl, String.class);
        sw.stop();
        return result;
    }
}
