package com.epam.epamgymdemo.service;

import com.epam.epamgymdemo.generator.UsernamePasswordGenerator;
import com.epam.epamgymdemo.model.bo.User;
import com.epam.epamgymdemo.model.dto.UserDto;
import com.epam.epamgymdemo.model.dto.UsernamePasswordTokenDto;
import com.epam.epamgymdemo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserService userService;

    @Test
    void testUsernamePasswordDto() {
        User user = User.builder().firstName("test").lastName("coverage").username("testCoverage").isActive(true).build();

        UsernamePasswordTokenDto usernamePasswordTokenDto = userService.usernameAndPassword(user, jwtService);

        assertNotNull(usernamePasswordTokenDto);
    }

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
        User user1 = User.builder().username("user1").firstName("John").lastName("Doe").isActive(true).build();
        User user2 = User.builder().username("user2").firstName("Jane").lastName("Smith").isActive(true).build();

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        when(userRepository.findAll()).thenReturn(users);

        List<UserDto> result = userService.getAll();

        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Doe", result.get(0).getLastName());
        assertEquals(true, result.get(0).getIsActive());
        assertEquals("Jane", result.get(1).getFirstName());
        assertEquals("Smith", result.get(1).getLastName());
        assertEquals(true, result.get(1).getIsActive());
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
