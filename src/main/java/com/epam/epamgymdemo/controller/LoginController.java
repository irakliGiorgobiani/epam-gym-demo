package com.epam.epamgymdemo.controller;

import com.epam.epamgymdemo.model.dto.UsernamePasswordDto;
import com.epam.epamgymdemo.service.AuthenticationService;
import com.epam.epamgymdemo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import javax.security.auth.login.CredentialNotFoundException;

@RestController
@AllArgsConstructor
@RequestMapping("/login/v1")
@Tag( name = "Login",
        description = "Operations for updating and validating user credentials in the application")
public class LoginController {

    private final AuthenticationService authenticationService;

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Validate user credentials",
            description = "check if the username and password given in the request headers are valid")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully validated credentials"),
            @ApiResponse(responseCode = "401", description = "Failed to validate credentials")
    })
    public ResponseEntity<UsernamePasswordDto> login(@RequestHeader(name = "username") String username,
                                                     @RequestHeader(name = "password") String password)
            throws CredentialNotFoundException {
        authenticationService.authenticateUser(username, password);
        return ResponseEntity.ok(authenticationService.usernamePassword(username, password));
    }

    @PostMapping("/change-password")
    @Operation(summary = "Change user password",
            description = "after validating the username and password, " +
                    "change the password by giving a new password in the path variable")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully changed user password"),
            @ApiResponse(responseCode = "401", description = "Failed to authorize")
    })
    public ResponseEntity<UsernamePasswordDto> changePassword(@RequestHeader(name = "username") String username,
                                                              @RequestHeader(name = "password") String oldPassword,
                                                              @RequestBody String newPassword)
            throws CredentialNotFoundException {
        authenticationService.authenticateUser(username, oldPassword);
        userService.changePassword(username, newPassword);
        return ResponseEntity.ok(authenticationService.usernamePassword(username, newPassword));
    }
}