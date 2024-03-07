package com.epam.epamgymdemo;

import com.epam.epamgymdemo.generator.UsernamePasswordGenerator;
import com.epam.epamgymdemo.model.User;
import com.epam.epamgymdemo.repository.UserRepository;
import com.epam.epamgymdemo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UsernamePasswordGenerator usernamePasswordGenerator;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setIsActive(true);

        when(usernamePasswordGenerator.generateUsername("John", "Doe")).thenReturn("johndoe");
        when(usernamePasswordGenerator.generatePassword()).thenReturn("password");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.create("John", "Doe", true);

        assertNotNull(createdUser);
        assertEquals("John", createdUser.getFirstName());
        assertEquals("Doe", createdUser.getLastName());
        assertEquals(true, createdUser.getIsActive());
        assertEquals("johndoe", createdUser.getUsername());
        assertEquals("password", createdUser.getPassword());
    }

    @Test
    void testGetUserById() throws InstanceNotFoundException {
        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = userService.getById(1L);

        assertNotNull(foundUser);
        assertEquals(user.getFirstName(), foundUser.getFirstName());
        assertEquals(user.getLastName(), foundUser.getLastName());
    }

    @Test
    void testGetAllUsers() {
        List<User> userList = new ArrayList<>();
        userList.add(new User());
        userList.add(new User());

        when(userRepository.findAll()).thenReturn(userList);

        List<User> foundUsers = userService.getAll();

        assertNotNull(foundUsers);
        assertEquals(userList.size(), foundUsers.size());
    }

    @Test
    void testGetByUsername() throws InstanceNotFoundException {
        User user = new User();
        user.setId(1L);
        user.setUsername("john_doe");

        when(userRepository.existsByUsername("john_doe")).thenReturn(true);
        when(userRepository.findByUsername("john_doe")).thenReturn(user);

        User foundUser = userService.getByUsername("john_doe");

        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
        assertEquals(user.getUsername(), foundUser.getUsername());
    }

    @Test
    void testGetByUsername_NotFound() {
        when(userRepository.existsByUsername("john_doe")).thenReturn(false);

        assertThrows(InstanceNotFoundException.class, () -> userService.getByUsername("john_doe"));
    }

    @Test
    void testChangePassword() throws InstanceNotFoundException {
        User user = new User();
        user.setId(1L);
        user.setPassword("old_password");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.changePassword(1L, "new_password");

        assertEquals("new_password", user.getPassword());
    }

    @Test
    void testChangeIsActive() throws InstanceNotFoundException {
        User user = new User();
        user.setId(1L);
        user.setIsActive(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.changeIsActive(1L, false);

        assertFalse(user.getIsActive());
    }

}
