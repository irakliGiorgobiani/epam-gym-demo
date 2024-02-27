package com.epam.epamgymdemo.generator;

import com.epam.epamgymdemo.dao.UserDao;
import com.epam.epamgymdemo.model.User;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;

public class UsernamePasswordGenerator {

    private final UserDao userDao;

    public UsernamePasswordGenerator(UserDao userDao) {
        this.userDao = userDao;
    }

    public String generateUsername(String firstName, String lastName) {
        String baseUsername = firstName + "." + lastName;
        String resultName;

        List<String> usernames = userDao.getAll().stream().map(User::getUserName).toList();
        long count = usernames.stream().filter(s -> s.contains(baseUsername)).count();

        if (usernames.contains(baseUsername)) {
            resultName = baseUsername + (count + 1);
        } else {
            resultName = baseUsername;
        }

        return resultName;
    }

    public String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }
}
