package com.epam.epamgymdemo.service;

import com.epam.epamgymdemo.dao.UserDao;
import com.epam.epamgymdemo.model.User;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;
@Service
public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User getByUsername(String username) throws InstanceNotFoundException {
        return userDao.getByUsername(username);
    }

    public void updatePassword(Long id, String password) throws InstanceNotFoundException {
        userDao.updatePassword(id, password);
    }
}
