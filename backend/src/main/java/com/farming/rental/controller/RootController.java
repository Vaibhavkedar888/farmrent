package com.farming.rental.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class RootController {

    @GetMapping("/")
    public ResponseEntity<Map<String, String>> home() {
        return ResponseEntity.ok(Map.of(
            "message", "AgroRent API is running",
            "status", "UP",
            "documentation", "Refer to project documentation for API endpoints"
        ));
    }
}
