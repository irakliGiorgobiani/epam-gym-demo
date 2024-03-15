package com.epam.epamgymdemo.controller;

import com.epam.epamgymdemo.model.dto.UsernamePasswordDto;
import com.epam.epamgymdemo.service.AuthenticationService;
import com.epam.epamgymdemo.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import javax.security.auth.login.CredentialNotFoundException;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private UserService userService;

    @InjectMocks
    private LoginController loginController;

    @Test
    public void testLogin() throws CredentialNotFoundException {
        String username = "test";
        String password = "password";
        UsernamePasswordDto expectedDto = new UsernamePasswordDto(username, password);
        when(authenticationService.usernamePassword(username, password)).thenReturn(expectedDto);

        ResponseEntity<UsernamePasswordDto> response = loginController.login(username, password);

        assertEquals(expectedDto, response.getBody());
    }

    @Test
    public void testChangePassword() throws CredentialNotFoundException {
        String username = "test";
        String oldPassword = "old";
        String newPassword = "new";

        loginController.changePassword(newPassword, username, oldPassword);

        verify(userService).changePassword(username, newPassword);
    }
}
