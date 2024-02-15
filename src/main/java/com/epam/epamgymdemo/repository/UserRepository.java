package com.epam.epamgymdemo.repository;

import com.epam.epamgymdemo.model.User;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepository {

    private final Map<Long, User> users;
    private final ResourceLoader resourceLoader;

    public UserRepository() {
        this.users = new HashMap<>();
        this.resourceLoader = new DefaultResourceLoader();
    }

    @PostConstruct
    private void loadDataFromFile() {
        try {
            Resource resource = resourceLoader.getResource("classpath:data/user.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] split = line.split(",");
                Long userId = Long.parseLong(split[0]);
                String firstName = split[1];
                String lastName = split[2];
                boolean isActive = Boolean.parseBoolean(split[3]);
                User user = new User(userId, firstName, lastName, isActive);
                users.put(userId, user);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User getUser(Long id) {
        return users.get(id);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public void removeUser(Long id) {
        users.remove(id);
    }

    public boolean containsUser(Long id) {
        return users.containsKey(id);
    }

    public boolean containsUser(User user) {
        return users.containsValue(user);
    }
}
