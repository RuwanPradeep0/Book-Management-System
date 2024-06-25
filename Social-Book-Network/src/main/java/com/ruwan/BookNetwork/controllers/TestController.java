package com.ruwan.BookNetwork.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestController {
    @GetMapping("/test1")
    public ResponseEntity<String> test() {
        System.out.println("Test endpoint hit");
        return ResponseEntity.ok("Test successful");
    }
}
