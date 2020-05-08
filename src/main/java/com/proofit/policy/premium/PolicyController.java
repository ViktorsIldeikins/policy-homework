package com.proofit.policy.premium;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PolicyController {

    @GetMapping("/test-path")
    public String test() {
        return "test response";
    }

}
