package com.epam.epamgymdemo.generator;

import com.epam.epamgymdemo.model.bo.User;
import com.epam.epamgymdemo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@AllArgsConstructor
public class UsernamePasswordGenerator {

    private final UserRepository userRepository;

    public String generateUsername(String firstName, String lastName) {
        String baseUsername = firstName + "." + lastName;
        String resultName;

        List<String> usernames = userRepository.findAll().stream().map(User::getUsername).toList();
        long count = usernames.stream().filter(s -> s.contains(baseUsername)).count();

        if (count != 0) {
            resultName = baseUsername + count;
        } else {
            resultName = baseUsername;
        }

        return resultName;
    }

    public String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }
}