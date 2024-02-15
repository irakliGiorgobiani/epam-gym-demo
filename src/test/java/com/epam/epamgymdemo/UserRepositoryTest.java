package com.epam.epamgymdemo;

import com.epam.epamgymdemo.model.User;
import com.epam.epamgymdemo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserRepositoryTest {

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository();
    }

    @Test
    void testGetUser() {
        User user =  new User(1L, "John", "Doe",  true);
        userRepository.addUser(user);

        assertEquals(user, userRepository.getUser(1L));
    }

    @Test
    void testGetAllUsers() {
        User user1 = new User(1L, "John", "Doe",  true);
        User user2 = new User(2L, "Jane", "Smith",  true);

        userRepository.addUser(user1);
        userRepository.addUser(user2);

        List<User> allUsers = userRepository.getAllUsers();

        assertTrue(allUsers.contains(user1));
        assertTrue(allUsers.contains(user2));
        assertEquals(2, allUsers.size());
    }

    @Test
    void testAddUser() {
        User user = new User(1L, "John", "Doe",  true);
        userRepository.addUser(user);

        assertTrue(userRepository.containsUser(1L));
    }

    @Test
    void testRemoveUser() {
        User user = new User(1L, "John", "Doe",  true);
        userRepository.addUser(user);
        userRepository.removeUser(1L);

        assertTrue(userRepository.getAllUsers().isEmpty());
    }

    @Test
    void testContainsUserById() {
        User user = new User(1L, "John", "Doe",  true);
        userRepository.addUser(user);

        assertTrue(userRepository.containsUser(1L));
    }

    @Test
    void testContainsUserByObject() {
        User user = new User(1L, "John", "Doe", true);
        userRepository.addUser(user);

        assertTrue(userRepository.containsUser(user));
    }
}
