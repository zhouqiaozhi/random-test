package com.zhou.importtest.controller;

import com.zhou.aop.annotation.MethodDesc;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping
    public String test() {
        return "test";
    }

    @GetMapping("/get/{id}")
    @MethodDesc(v = "test-get")
    public String testGet(@PathVariable("id") String id) {
        return "test";
    }
    @PostMapping
    @MethodDesc(v = "test-post")
    public String testPost(@RequestBody Map<String, String> body) {
        return "test";
    }
    @PostMapping("/empty")
    @MethodDesc(v = "test-post-empty-body")
    public String testPostEmptyBody() {
        return "test";
    }
    @PutMapping
    @MethodDesc(v = "test-put")
    public String testPut() {
        return "test";
    }
}
