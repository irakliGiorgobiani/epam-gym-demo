package com.epam.epamgymdemo.model;

import lombok.Getter;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashMap;
import java.util.Map;

@Getter
public class User {
    private final Long userId;
    private final String firstName;
    private final String lastName;
    private final String userName;
    private final String password;
    private final boolean isActive;
    private static Map<String, Integer> usernameCount = new HashMap<>();

    public User(Long userId, String firstName, String lastName, boolean isActive) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = isActive;
        this.userName = generateUsername(firstName, lastName);
        this.password = generatePassword();
    }

    private String generateUsername(String firstName, String lastName) {
        String baseUsername = firstName + "." + lastName;
        String resultName = "";

        if (usernameCount.containsKey(baseUsername)) {
            resultName = baseUsername + (usernameCount.get(baseUsername) + 1);
            int newCount = usernameCount.get(baseUsername) + 1;
            usernameCount.put(baseUsername, newCount);
        } else {
            usernameCount.putIfAbsent(baseUsername, 1);
            resultName = baseUsername;
        }

        return resultName;
    }

    private String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }
}
