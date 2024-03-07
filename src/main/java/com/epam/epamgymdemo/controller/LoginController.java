package com.epam.epamgymdemo.controller;

import com.epam.epamgymdemo.facade.GymFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceNotFoundException;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final GymFacade gymFacade;

    public LoginController(GymFacade gymFacade) {
        this.gymFacade = gymFacade;
    }

    @GetMapping
    public ResponseEntity<String> login(@RequestHeader String username, @RequestHeader String password) throws InstanceNotFoundException {
        gymFacade.authorize(username, password);

        return ResponseEntity.ok("Authorization has been successful");
    }

    @PostMapping("/change-password/{newPassword}")
    public ResponseEntity<String> changePassword(@PathVariable String newPassword, @RequestHeader String username, @RequestHeader(name = "password") String oldPassword) throws InstanceNotFoundException {

        gymFacade.changePassword(username, oldPassword, newPassword);

        return ResponseEntity.ok(String.format("Password has been successfully changed to %s", newPassword));
    }
}
