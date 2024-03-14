package com.epam.epamgymdemo.controller;

import com.epam.epamgymdemo.model.dto.UserDto;
import com.epam.epamgymdemo.service.AuthenticationService;
import com.epam.epamgymdemo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceNotFoundException;
import javax.security.auth.login.CredentialNotFoundException;

@RestController
@AllArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final AuthenticationService authenticationService;

    private final UserService userService;

    @GetMapping
    public ResponseEntity<String> login(@RequestHeader(name = "username") String username, @RequestHeader(name = "password") String password) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.authenticateUser(username, password);
        return ResponseEntity.ok("Authorization has been successful");
    }

    @PostMapping("/change-password/{newPassword}")
    public ResponseEntity<String> changePassword(@PathVariable String newPassword, @RequestHeader(name = "username") String username, @RequestHeader(name = "password") String oldPassword) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.authenticateUser(username, oldPassword);

        userService.changePassword(username, newPassword);

        return ResponseEntity.ok(String.format("Password has been successfully changed to %s", newPassword));
    }
}
