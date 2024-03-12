package com.epam.epamgymdemo;

import com.epam.epamgymdemo.generator.UsernamePasswordGenerator;
import com.epam.epamgymdemo.model.User;
import com.epam.epamgymdemo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class UsernamePasswordGeneratorTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UsernamePasswordGenerator usernamePasswordGenerator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGenerateUsername() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        List<User> users = new ArrayList<>();
        User user1 = User.builder()
                .id(1L)
                .firstName(firstName)
                .lastName(lastName)
                .username("John.Doe")
                .isActive(true)
                .build();
        users.add(user1);

        User user2 = User.builder()
                .id(2L)
                .firstName(firstName)
                .lastName(lastName)
                .username("John.Doe1")
                .isActive(true)
                .build();
        users.add(user2);

        when(userRepository.findAll()).thenReturn(users);

        String result = usernamePasswordGenerator.generateUsername(firstName, lastName);

        assertEquals("John.Doe2", result);
    }

    @Test
    public void testGeneratePassword() {
        String result = usernamePasswordGenerator.generatePassword();

        assertEquals(10, result.length());
    }
}