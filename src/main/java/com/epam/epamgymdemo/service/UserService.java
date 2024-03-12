package com.epam.epamgymdemo.service;

import com.epam.epamgymdemo.generator.UsernamePasswordGenerator;
import com.epam.epamgymdemo.model.User;
import com.epam.epamgymdemo.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.InstanceNotFoundException;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UsernamePasswordGenerator usernamePasswordGenerator;

    @Transactional
    public User create(String firstName, String lastName, Boolean isActive) throws NamingException {
        if (firstName == null) {
            throw new NameNotFoundException("The first name was not provided");
        } else if (firstName.length() < 1 || firstName.length() > 50) {
            throw new NamingException(String.format("the first name: %s is too short", firstName));
        } else if (!firstName.matches("[a-zA-Z]+")) {
            throw new NamingException(String.format("The first name: %s must only contain letters", firstName));
        }

        if (lastName == null) {
            throw new NameNotFoundException("The last name was not provided");
        } else if (lastName.length() < 1 || lastName.length() > 50) {
            throw new NamingException(String.format("the last name: %s is too short", lastName));
        } else if (!lastName.matches("[a-zA-Z]+")) {
            throw new NamingException(String.format("The last name: %s must only contain letters", lastName));
        }

        if (isActive == null) {
            isActive = false;
        }

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
    public void changePassword(Long id, String password) throws InstanceNotFoundException {
        User user = this.getById(id);

        user.setPassword(password);

        userRepository.save(user);

        log.info(String.format("Password changed successfully for the user with the id: %d", user.getId()));
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
    public void changeIsActive(Long id) throws InstanceNotFoundException {
        User user = this.getById(id);

        user.setIsActive(!user.getIsActive());

        userRepository.save(user);

        log.info(String.format("IsActive changed successfully for the user with the id: %d", user.getId()));
    }
}
