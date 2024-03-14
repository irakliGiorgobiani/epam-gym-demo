package com.epam.epamgymdemo.service;

import com.epam.epamgymdemo.exception.MissingPropertyException;
import com.epam.epamgymdemo.generator.UsernamePasswordGenerator;
import com.epam.epamgymdemo.model.bo.User;
import com.epam.epamgymdemo.model.dto.UserDto;
import com.epam.epamgymdemo.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.InstanceNotFoundException;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UsernamePasswordGenerator usernamePasswordGenerator;

    private UserDto createDto(User user) {
        return UserDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .isActive(user.getIsActive())
                .password(user.getPassword())
                .build();
    }

    private UserDto validateFields(UserDto userDto) throws NamingException {
        if (userDto.getFirstName() == null) {
            throw new NameNotFoundException("The first name was not provided");
        } else if (userDto.getFirstName().length() < 1 || userDto.getFirstName().length() > 50) {
            throw new NamingException(String.format("the first name: %s is too short", userDto.getFirstName()));
        } else if (!userDto.getFirstName().matches("[a-zA-Z]+")) {
            throw new NamingException
                    (String.format("The first name: %s must only contain letters", userDto.getFirstName()));
        }

        if (userDto.getLastName() == null) {
            throw new NameNotFoundException("The last name was not provided");
        } else if (userDto.getLastName().length() < 1 || userDto.getLastName().length() > 50) {
            throw new NamingException(String.format("the last name: %s is too short", userDto.getLastName()));
        } else if (!userDto.getLastName().matches("[a-zA-Z]+")) {
            throw new NamingException
                    (String.format("The last name: %s must only contain letters", userDto.getLastName()));
        }

        return userDto;
    }

    @Transactional
    public User create(UserDto createdUserDto) throws NamingException {
        UserDto userDto = validateFields(createdUserDto);

        userDto.setUsername(usernamePasswordGenerator.generateUsername(userDto.getFirstName(), userDto.getLastName()));
        userDto.setPassword(usernamePasswordGenerator.generatePassword());

        User user = User.builder()
                        .firstName(userDto.getFirstName())
                        .lastName(userDto.getLastName())
                        .isActive(true)
                        .username(userDto.getUsername())
                        .password(userDto.getPassword())
                        .build();

        userRepository.save(user);

        log.info(String.format("User with the id: %d successfully created", user.getId()));

        return user;
    }

    public Map<String, String> usernameAndPassword(User user) {
        return Map.of("username", user.getUsername(), "password", user.getPassword());
    }

    @Transactional
    public void update(String username, UserDto updatedUserDto) throws InstanceNotFoundException, NamingException {
        UserDto userDto = validateFields(updatedUserDto);

        if (userDto.getIsActive() == null) {
            throw new MissingPropertyException("The isActive field was not provided");
        }

        User user = this.getByUsername(username);

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setIsActive(userDto.getIsActive());

        userRepository.save(user);

        log.info(String.format("The user with the id: %d has been successfully updated", user.getId()));
    }

    public User getById(Long id) throws InstanceNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new InstanceNotFoundException(String.format("User not found with the id: %d", id)));
    }

    public List<UserDto> getAll() {
        List<UserDto> userDtoList = new ArrayList<>();

        for (User user : userRepository.findAll()) {
            userDtoList.add(createDto(user));
        }

        return userDtoList;
    }

    public User getByUsername(String username) throws InstanceNotFoundException {
        return Optional.of(userRepository.findByUsername(username))
                .orElseThrow(() -> new InstanceNotFoundException
                        (String.format("User not found with the username: %s", username)));
    }

    @Transactional
    public void changePassword(String username, String password) throws InstanceNotFoundException {
        User user = this.getByUsername(username);

        user.setPassword(password);

        userRepository.save(user);

        log.info(String.format("Password changed successfully for the user with the id: %d", user.getId()));
    }

    @Transactional
    public UserDto toggleActivation(Long id) throws InstanceNotFoundException {
        User user = this.getById(id);

        user.setIsActive(!user.getIsActive());

        userRepository.save(user);

        log.info(String.format("IsActive changed successfully for the user with the id: %d", user.getId()));

        return createDto(user);
    }

    @Transactional
    public Map<String, Object> changeActive(String username, Boolean isActive) throws InstanceNotFoundException {
        User user = this.getByUsername(username);

        user.setIsActive(isActive);

        return Map.of("isActive", user.getIsActive());
    }
}
