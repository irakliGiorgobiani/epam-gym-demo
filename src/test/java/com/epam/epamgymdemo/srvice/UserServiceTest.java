package com.epam.epamgymdemo.srvice;

import com.epam.epamgymdemo.generator.UsernamePasswordGenerator;
import com.epam.epamgymdemo.model.bo.User;
import com.epam.epamgymdemo.repository.UserRepository;
import com.epam.epamgymdemo.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.management.InstanceNotFoundException;
import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UsernamePasswordGenerator usernamePasswordGenerator;

    @InjectMocks
    private UserService userService;

    @Test
    void testCreateSuccess() throws NamingException {
        String firstName = "John";
        String lastName = "Doe";
        Boolean isActive = true;
        String username = "John.Doe";
        String password = "password";

        when(usernamePasswordGenerator.generateUsername(firstName, lastName)).thenReturn(username);
        when(usernamePasswordGenerator.generatePassword()).thenReturn(password);

        User createdUser = userService.create(firstName, lastName, isActive);

        assertNotNull(createdUser);
        assertEquals(firstName, createdUser.getFirstName());
        assertEquals(lastName, createdUser.getLastName());
        assertEquals(isActive, createdUser.getIsActive());
        assertEquals(username, createdUser.getUsername());
        assertEquals(password, createdUser.getPassword());
    }

    @Test
    void testCreateFailure() {
        String firstName = "";
        String lastName = "lastName";
        Boolean isActive = true;

        assertThrows(NamingException.class, () -> userService.create(firstName, lastName, isActive));
    }


    @Test
    void testGetById() throws InstanceNotFoundException {
        User user = User.builder()
                .id(1L)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = userService.getById(1L);

        assertNotNull(foundUser);
        assertEquals(user, foundUser);
    }

    @Test
    void testGetAll() {
        List<User> userList = new ArrayList<>();
        userList.add(new User());
        userList.add(new User());

        when(userRepository.findAll()).thenReturn(userList);

        List<User> foundUsers = userService.getAll();

        assertEquals(userList, foundUsers);
    }

    @Test
    void testGetByUsernameSuccess() throws InstanceNotFoundException {
        String username = "john_doe";

        User user = User.builder()
                .username(username)
                .build();

        when(userRepository.existsByUsername(username)).thenReturn(true);
        when(userRepository.findByUsername(username)).thenReturn(user);

        User foundUser = userService.getByUsername(username);

        assertNotNull(foundUser);
        assertEquals(user, foundUser);
    }

    @Test
    void testGetByUsernameFailure() {
        when(userRepository.existsByUsername("john_doe")).thenReturn(false);

        assertThrows(InstanceNotFoundException.class, () -> userService.getByUsername("john_doe"));
    }

    @Test
    void testChangePassword() throws InstanceNotFoundException {
        User user = User.builder()
                .id(1L)
                .password("password")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.changePassword(1L, "new_password");

        assertEquals("new_password", user.getPassword());
    }

    @Test
    void testChangeIsActive() throws InstanceNotFoundException {
        User user = User.builder()
                .id(1L)
                .isActive(true)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.toggleActivation(1L);

        assertFalse(user.getIsActive());
    }
}
