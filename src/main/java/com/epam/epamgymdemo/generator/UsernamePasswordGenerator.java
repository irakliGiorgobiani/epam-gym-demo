package com.epam.epamgymdemo.generator;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Map;

public class UsernamePasswordGenerator<T> {
    public String generateUsername(String firstName, String lastName, Map<String, T> repo) {
        String baseUsername = firstName + "." + lastName;

        int count = 0;

        for (String key : repo.keySet()) {
            if (key.contains(baseUsername)) {
                count++;
            }
        }

        if (count == 0) {
            return baseUsername;
        } else {
            return baseUsername + (count + 1);
        }
    }

    public String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }
}
