package com.epam.epamgymdemo.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceNotFoundException;
import javax.security.auth.login.CredentialNotFoundException;

@RestController
@AllArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final GymFacade gymFacade;

    @GetMapping
    public ResponseEntity<String> login(@RequestHeader(name = "username") String username, @RequestHeader(name = "password") String password) throws InstanceNotFoundException, CredentialNotFoundException {
        gymFacade.authenticate(username, password);

        return ResponseEntity.ok("Authorization has been successful");
    }

    @PostMapping("/change-password/{newPassword}")
    public ResponseEntity<String> changePassword(@PathVariable String newPassword, @RequestHeader(name = "username") String username, @RequestHeader(name = "password") String oldPassword) throws InstanceNotFoundException, CredentialNotFoundException {
        String token = gymFacade.authenticate(username, oldPassword);

        gymFacade.changePassword(username, newPassword, token);

        return ResponseEntity.ok(String.format("Password has been successfully changed to %s", newPassword));
    }
}
