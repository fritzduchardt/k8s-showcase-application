package com.fduchardt.k8sshowcase.web;

import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.*;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Slf4j
@Profile("!nodb")
public class DatabaseController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @PostMapping(path = "/database")
    public String forward(@RequestBody  String data) {
        jdbcTemplate.update("INSERT into k8sshowcase (data) values(?)", data);
        return "done";
    }
}
