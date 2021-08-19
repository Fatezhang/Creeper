package com.demo.graphql.demographql.restful;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest")
public class RestfulController {

    @PostMapping("/heartbeat")
    public ResponseEntity<String> heartbeat(@Validated(BasicInfo.class) @RequestBody RestRequest request) {
        System.out.println("request = " + request);
        return ResponseEntity.ok("OK");
    }
}
