package com.epam.epamgymdemo.service;

import com.epam.epamgymdemo.generator.UsernamePasswordGenerator;
import com.epam.epamgymdemo.model.User;
import com.epam.epamgymdemo.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.InstanceNotFoundException;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UsernamePasswordGenerator usernamePasswordGenerator;

    @Transactional
    public User create(String firstName, String lastName, Boolean isActive) {
        String username = usernamePasswordGenerator.generateUsername(firstName, lastName);
        String password = usernamePasswordGenerator.generatePassword();

        User user = User.builder()
                        .firstName(firstName)
                        .lastName(lastName)
                        .isActive(isActive)
                        .username(username)
                        .password(password)
                        .build();

        userRepository.save(user);

        log.info(String.format("User with the id: %d successfully created", user.getId()));

        return user;
    }

    public User getById(Long id) throws InstanceNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new InstanceNotFoundException(String.format("User not found with the id: %d", id)));
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getByUsername(String username) throws InstanceNotFoundException {
        if (!userRepository.existsByUsername(username)) {
            throw new InstanceNotFoundException(String.format("User not found with the username: %s", username));
        } else return userRepository.findByUsername(username);
    }

    @Transactional
    public void changeUsername(Long id, String username) throws InstanceNotFoundException {
        User user = this.getById(id);

        user.setUsername(username);

        userRepository.save(user);
    }

    @Transactional
    public void changeFirstName(Long id, String firstName) throws InstanceNotFoundException {
        User user = this.getById(id);

        user.setFirstName(firstName);

        userRepository.save(user);
    }

    @Transactional
    public void changeLastName(Long id, String lastName) throws InstanceNotFoundException {
        User user = this.getById(id);

        user.setLastName(lastName);

        userRepository.save(user);
    }

    @Transactional
    public void changePassword(Long id, String password) throws InstanceNotFoundException {
        User user = this.getById(id);

        user.setPassword(password);

        userRepository.save(user);

        log.info(String.format("Password changed successfully for the user with the id: %d", user.getId()));
    }

    @Transactional
    public void changeIsActive(Long id, Boolean isActive) throws InstanceNotFoundException {
        User user = this.getById(id);

        user.setIsActive(isActive);

        userRepository.save(user);

        log.info(String.format("IsActive changed successfully for the user with the id: %d", user.getId()));
    }
}
