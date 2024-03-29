package com.epam.epamgymdemo.service;

import com.epam.epamgymdemo.exception.MissingFieldException;
import com.epam.epamgymdemo.exception.EntityNotFoundException;
import com.epam.epamgymdemo.exception.NamingException;
import com.epam.epamgymdemo.generator.UsernamePasswordGenerator;
import com.epam.epamgymdemo.model.bo.User;
import com.epam.epamgymdemo.model.dto.ActiveDto;
import com.epam.epamgymdemo.model.dto.UserDto;
import com.epam.epamgymdemo.model.dto.UsernamePasswordTokenDto;
import com.epam.epamgymdemo.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final UsernamePasswordGenerator usernamePasswordGenerator;

    private UserDto validateFields(UserDto userDto) {
        if (userDto.getFirstName() == null) {
            throw new MissingFieldException("The first name was not provided");
        } else if (userDto.getFirstName().length() < 2 || userDto.getFirstName().length() > 50) {
            throw new NamingException(String.format("the first name: %s is too short", userDto.getFirstName()));
        } else if (!userDto.getFirstName().matches("[a-zA-Z]+")) {
            throw new NamingException
                    (String.format("The first name: %s must only contain letters", userDto.getFirstName()));
        }

        if (userDto.getLastName() == null) {
            throw new MissingFieldException("The last name was not provided");
        } else if (userDto.getLastName().length() < 2 || userDto.getLastName().length() > 50) {
            throw new NamingException(String.format("the last name: %s is too short", userDto.getLastName()));
        } else if (!userDto.getLastName().matches("[a-zA-Z]+")) {
            throw new NamingException
                    (String.format("The last name: %s must only contain letters", userDto.getLastName()));
        }

        return userDto;
    }

    public UsernamePasswordTokenDto usernameAndPassword(User user, JwtService jwtService) {
        return UsernamePasswordTokenDto.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .token(jwtService.generateToken(user))
                .build();
    }

    @Transactional
    public User create(UserDto createdUserDto) {
        UserDto userDto = validateFields(createdUserDto);

        User user = User.builder()
                        .firstName(userDto.getFirstName())
                        .lastName(userDto.getLastName())
                        .isActive(true)
                        .username(usernamePasswordGenerator.generateUsername
                                (userDto.getFirstName(), userDto.getLastName()))
                        .password(usernamePasswordGenerator.generatePassword())
                        .build();

        userRepository.save(user);

        log.info(String.format("User with the id: %d successfully created", user.getId()));

        return user;
    }

    @Transactional
    public void update(String username, UserDto updatedUserDto) {
        UserDto userDto = validateFields(updatedUserDto);

        if (userDto.getIsActive() == null) {
            throw new MissingFieldException("The isActive field was not provided");
        }

        User user = this.getByUsername(username);

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setIsActive(userDto.getIsActive());

        userRepository.save(user);

        log.info(String.format("The user with the id: %d has been successfully updated", user.getId()));
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException
                        (String.format("User not found with the username: %s", username)));
    }

    public List<UserDto> getAll() {
        List<UserDto> userDtoList = new ArrayList<>();

        for (User user : userRepository.findAll()) {
            userDtoList.add(UserDto.builder()
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .username(user.getUsername())
                    .isActive(user.getIsActive())
                    .build());
        }

        return userDtoList;
    }

    @Transactional
    public void changePassword(String username, String password) {
        User user = this.getByUsername(username);

        user.setPassword(password);

        userRepository.save(user);

        log.info(String.format("Password changed successfully for the user with the id: %d", user.getId()));
    }

    @Transactional
    public ActiveDto changeActive(String username, Boolean isActive) {
        User user = this.getByUsername(username);

        user.setIsActive(isActive);

        return ActiveDto.builder().isActive(user.getIsActive()).build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.getByUsername(username);
    }
}