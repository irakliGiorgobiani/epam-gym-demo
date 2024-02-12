package com.epam.epamgymdemo.repository.storage;

import com.epam.epamgymdemo.model.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRepository {

    private final Map<String, User> users;

    public UserRepository() {
        this.users = new HashMap<>();
    }

    public Map<String, User> getUsers() {
        return users;
    }
}
