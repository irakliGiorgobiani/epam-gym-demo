package com.epam.epamgymdemo.service;

import com.epam.epamgymdemo.generator.UsernamePasswordGenerator;
import com.epam.epamgymdemo.model.bo.User;
import com.epam.epamgymdemo.model.dto.UserDto;
import com.epam.epamgymdemo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UsernamePasswordGenerator usernamePasswordGenerator;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testCreate() {
        UserDto userDto = UserDto.builder()
                .firstName("John")
                .lastName("Doe")
                .isActive(true)
                .build();

        when(usernamePasswordGenerator.generatePassword()).thenReturn("password");

        userService.create(userDto);

        verify(userRepository).save(any(User.class));
    }

    @Test
    void testUpdate() {
        UserDto userDto = UserDto.builder()
                .firstName("Jane")
                .lastName("Smith")
                .isActive(false)
                .build();

        User existingUser = User.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Doe")
                .isActive(true)
                .build();

        when(userRepository.findByUsername("jane.doe")).thenReturn(Optional.of(existingUser));

        userService.update("jane.doe", userDto);

        assertEquals("Jane", existingUser.getFirstName());
        assertEquals("Smith", existingUser.getLastName());
        assertFalse(existingUser.getIsActive());
        verify(userRepository).save(existingUser);
    }

    @Test
    void testGetByUsername() {
        User expectedUser = User.builder()
                .firstName("John")
                .lastName("Doe")
                .isActive(true)
                .build();

        when(userRepository.findByUsername("john.doe")).thenReturn(Optional.of(expectedUser));

        User user = userService.getByUsername("john.doe");

        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertTrue(user.getIsActive());
    }

    @Test
    void testGetAll() {
        userService.getAll();

        verify(userRepository).findAll();
    }

    @Test
    void testChangePassword() {
        User existingUser = User.builder().username("john.doe").password("oldPassword").build();

        when(userRepository.findByUsername("john.doe")).thenReturn(Optional.of(existingUser));

        userService.changePassword("john.doe", "newPassword");

        assertEquals("newPassword", existingUser.getPassword());
    }

    @Test
    void testChangeActive() {
        User existingUser = User.builder().username("john.doe").isActive(true).build();

        when(userRepository.findByUsername("john.doe")).thenReturn(Optional.of(existingUser));

        userService.changeActive("john.doe", false);

        assertFalse(existingUser.getIsActive());
    }
}
