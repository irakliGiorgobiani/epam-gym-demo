package com.epam.epamgymdemo.controller;

import com.epam.epamgymdemo.model.dto.UsernamePasswordTokenDto;
import com.epam.epamgymdemo.service.AuthenticationService;
import com.epam.epamgymdemo.service.JwtService;
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

@RestController
@AllArgsConstructor
@RequestMapping("/login/v1")
@Tag( name = "Login",
        description = "Operations for updating and validating user credentials in the application")
public class LoginController {

    private final AuthenticationService authenticationService;

    private final UserService userService;

    private final JwtService jwtService;

    @GetMapping
    @Operation(summary = "Validate user credentials",
            description = "check if the username and password given in the request headers are valid")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully validated credentials"),
            @ApiResponse(responseCode = "401", description = "Failed to validate credentials")
    })
    public ResponseEntity<UsernamePasswordTokenDto> login(@RequestHeader(name = "username") String username,
                                                          @RequestHeader(name = "password") String password) {
        authenticationService.authenticateUser(username, password);
        return ResponseEntity.ok(userService.usernameAndPassword(userService.getByUsername(username),
                jwtService));
    }

    @PostMapping("/change-password")
    @Operation(summary = "Change user password",
            description = "after validating the username and password, " +
                    "change the password by giving a new password in the path variable")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully changed user password"),
            @ApiResponse(responseCode = "401", description = "Failed to authorize")
    })
    public ResponseEntity<UsernamePasswordTokenDto> changePassword(@RequestHeader(name = "username") String username,
                                                                   @RequestHeader(name = "password") String oldPassword,
                                                                   @RequestBody String newPassword) {
        authenticationService.authenticateUser(username, oldPassword);
        userService.changePassword(username, newPassword);
        return ResponseEntity.ok(userService.usernameAndPassword(userService.getByUsername(username), jwtService));
    }
}