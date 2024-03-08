package com.epam.epamgymdemo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/training")
public class TrainingController {

    @PostMapping("/add")
    public ResponseEntity<String> addTraining(@RequestBody Map<String, String> requestBody,
                                              @RequestHeader(name = "username") String usernameAuth,
                                              @RequestHeader(name = "password") String password) {
        return null;
    }
}
