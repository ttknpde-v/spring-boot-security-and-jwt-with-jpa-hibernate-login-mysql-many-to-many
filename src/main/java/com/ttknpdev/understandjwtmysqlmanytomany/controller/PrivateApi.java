package com.ttknpdev.understandjwtmysqlmanytomany.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/private")
public class PrivateApi {
    @GetMapping("/test")
    public ResponseEntity<?> test () {
        return ResponseEntity.ok("hello world");
    }
}
