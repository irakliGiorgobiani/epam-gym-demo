package com.epam.epamgymdemo;

import com.epam.epamgymdemo.dao.UserDao;
import com.epam.epamgymdemo.model.User;
import com.epam.epamgymdemo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;

import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserDaoTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDao userDao;

    private User user;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .userName("johndoe")
                .password("password")
                .isActive(true)
                .build();
    }

    @Test
    public void testCreateUser() {
        when(userRepository.existsById(user.getId())).thenReturn(false);

        userDao.create(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testCreateUserAlreadyExists() {
        when(userRepository.existsById(user.getId())).thenReturn(true);

        assertThrows(DuplicateKeyException.class, () -> userDao.create(user));
    }

    @Test
    public void testGetUserById() throws InstanceNotFoundException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userDao.get(1L);

        assertEquals(user, result);
    }

    @Test
    public void testGetUserByIdNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(InstanceNotFoundException.class, () -> userDao.get(1L));
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = new ArrayList<>();
        users.add(user);
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userDao.getAll();

        assertEquals(users, result);
    }

    @Test
    public void testGetByUsername() throws InstanceNotFoundException {
        when(userRepository.existsByUserName("johndoe")).thenReturn(true);
        when(userRepository.findByUserName("johndoe")).thenReturn(user);

        User result = userDao.getByUsername("johndoe");

        assertEquals(user, result);
    }

    @Test
    public void testGetByUsernameNotFound() {
        when(userRepository.existsByUserName("johndoe")).thenReturn(false);

        assertThrows(InstanceNotFoundException.class, () -> userDao.getByUsername("johndoe"));
    }

    @Test
    public void testUpdatePassword() throws InstanceNotFoundException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userDao.updatePassword(1L, "newpassword");

        assertEquals("newpassword", user.getPassword());
    }

    @Test
    public void testUpdateIsActive() throws InstanceNotFoundException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userDao.updateIsActive(1L, false);

        assertEquals(false, user.getIsActive());
    }
}

